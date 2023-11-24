package com.zenmo.dbutil

import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.CustomStringFunction
import org.jetbrains.exposed.sql.EqOp
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.ExpressionWithColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.jetbrains.exposed.sql.statements.jdbc.JdbcPreparedStatementImpl
import org.jetbrains.exposed.sql.stringLiteral
import java.io.Serializable
import kotlin.Array
import kotlin.reflect.KClass
import java.sql.Array as SQLArray

public fun <T : Enum<T>> Table.enumArray(
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
public infix fun String.equalsAny(other: Expression<Array<String>>): EqOp =
    stringLiteral(this) eqAny other

/**
 * Invokes the `ANY` function on [expression].
 */
public fun <T : Serializable> any(
    expression: Expression<Array<T>>,
): ExpressionWithColumnType<String?> = CustomStringFunction("ANY", expression)

private infix fun <T : Serializable> Expression<T>.eqAny(other: Expression<Array<T>>): EqOp = EqOp(this, any(other))

/**
 * Implementation of PostgreSQL enum arrays.
 *
 * TODO: this can probably be generalized to any array type.
 * The serialization of the element would then be delegated to another [ColumnType].
 */
public class EnumArrayColumnType<T : Enum<T>>(
    private val enumClass: KClass<T>,
    private val size: Int? = null,
    private val databaseType: String = enumClass.simpleName!!,
    private val stringToEnumMap: Map<String, T> = enumClass.java.enumConstants.map { it.name to it }.toMap(),
    private val stringToEnum: (String) -> T? = stringToEnumMap::get,
    private val enumToString: (T) -> String = {enum -> enum.name}
) : ColumnType() {
    override fun sqlType(): String = "$databaseType ARRAY${size?.let { "[$it]" } ?: ""}"

    /**
     * It calls [notNullValueToDB] first with the List,
     * then it calls [setParameter],
     * and then it calls [notNullValueToDB] for each element.
     *
     * I'm not sure if we've implemented it correctly.
     */
    override fun notNullValueToDB(value: Any): Any {
        return when (value) {
            is Enum<*> -> value
            is Collection<*> -> value.map { enumToString(it as T) }.toTypedArray()
            is Array<*> -> value.map { enumToString(it as T) }.toTypedArray()
            else -> error("Got unexpected array value of type: ${value::class.qualifiedName} ($value)")
        }
    }

    override fun valueFromDB(value: Any): List<T> {
        val stringList: List<String> = when (value) {
            is SQLArray -> (value.array as Array<String>).toList()
            is Array<*> -> (value as Array<String>).toList()
            is Collection<*> -> (value as Array<String>).toList()
            else -> error("Got unexpected array value of type: ${value::class.qualifiedName} ($value)")
        }

        return stringList.map { stringToEnum(it) ?: error("Could not find enum value for $it") }
    }

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        println("setParameter: $value")
        if (value == null) {
            stmt.setNull(index, this)
        } else {
            val preparedStatement = stmt as? JdbcPreparedStatementImpl ?: error("Currently only JDBC is supported")
            val array = preparedStatement.statement.connection.createArrayOf(databaseType, value as Array<*>)
            stmt[index] = array
        }
    }
}
