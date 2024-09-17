package com.zenmo.orm.dbutil

import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.CustomStringFunction
import org.jetbrains.exposed.sql.EqOp
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.ExpressionWithColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.jetbrains.exposed.sql.statements.jdbc.JdbcPreparedStatementImpl
import org.jetbrains.exposed.sql.stringLiteral
import org.jetbrains.exposed.sql.vendors.H2Dialect
import org.jetbrains.exposed.sql.vendors.currentDialect
import java.io.Serializable
import kotlin.Array
import kotlin.reflect.KClass
import java.sql.Array as SQLArray

fun <T : Enum<T>> Table.enumArray(
    name: String,
    enumClass: KClass<T>,
) =
    registerColumn<List<T>>(name, EnumArrayColumnType<T>(enumClass))


/**
 * Checks whether this string is in the [other] expression.
 *
 * Example:
 * ```kotlin
 * productService.find { "tag" eqAny ProductsTable.tags }
 * ```
 *
 * @see any
 */
infix fun String.equalsAny(other: Expression<Array<String>>): EqOp =
    stringLiteral(this) eqAny other

/**
 * Invokes the `ANY` function on [expression].
 */
fun <T : Serializable> any(
    expression: Expression<Array<T>>,
): ExpressionWithColumnType<String?> = CustomStringFunction("ANY", expression)

private infix fun <T : Serializable> Expression<T>.eqAny(other: Expression<Array<T>>): EqOp = EqOp(this, any(other))

/**
 * Implementation of PostgreSQL enum arrays.
 *
 * TODO: this can probably be generalized to any array type.
 * TODO: can maybe extend org.jetbrains.exposed.sql.ArrayColumnType
 * The serialization of the element would then be delegated to another [ColumnType].
 */
class EnumArrayColumnType<T : Enum<T>>(
    private val enumClass: KClass<T>,
    private val size: Int? = null,
    private val databaseType: String = enumClass.simpleName!!,
    private val stringToEnumMap: Map<String, T> = enumClass.java.enumConstants.associateBy { it.name },
    private val stringToEnum: (String) -> T? = stringToEnumMap::get,
    private val enumToString: (T) -> String = {enum -> enum.name}
) : ColumnType<List<T>>() {
    override fun sqlType(): String = "$databaseType ARRAY${size?.let { "[$it]" } ?: ""}"

    /**
     * It calls [notNullValueToDB] first with the List,
     * then it calls [setParameter],
     * and then it calls [notNullValueToDB] for each element.
     *
     * I'm not sure if we've implemented it correctly.
     */
    override fun notNullValueToDB(value: List<T>): Any {
        return when (value) {
            is Enum<*> -> value
            is Collection<*> -> value.map { enumToString(it as T) }.toTypedArray()
//            is Array<*> -> value.map { enumToString(it as T) }.toTypedArray()
            else -> error("Got unexpected array value of type: ${value::class.qualifiedName} ($value)")
        }
    }

    override fun nonNullValueToString(value: List<T>): String {
        return value.joinToString(",", "ARRAY[", "]::${databaseType}[]") { enumToString(it) }
    }

    override fun valueFromDB(value: Any): List<T> {
        val stringList: List<String> = when (value) {
            is SQLArray -> (value.array as Array<String>).toList()
            is Array<*> -> (value as Array<String>).toList()
            is Collection<*> -> (value as Collection<String>).toList()
            else -> error("Got unexpected array value of type: ${value::class.qualifiedName} ($value)")
        }

        return stringList.map { stringToEnum(it) ?: error("Could not find enum value for $it") }
    }

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        when (value) {
            null -> stmt.setNull(index, this)
            is Array<*> -> {
                stmt.setArray(index, databaseType, value)
            }
            else -> throw Exception("Got unexpected array value of type: ${value::class.qualifiedName} ($value)")
        }
    }
}
