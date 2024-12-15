package com.zenmo.orm.dbutil

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.CustomFunction
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.UUIDColumnType
import java.util.UUID
import kotlin.uuid.Uuid

val postgresGenRandomUuid = CustomFunction("gen_random_uuid", UUIDColumnType())

/**
 * Table which generates ID on database side for convenience in external tools.
 */
open class ZenmoUUIDTable(name: String, columnName: String = "id") : Table(name) {
    val id = uuid(columnName).autoGenerateUuid()
    override val primaryKey = PrimaryKey(id)

    fun Column<UUID>.autoGenerateUuid(): Column<UUID> = apply {
        autoGenerate()
        defaultExpression(postgresGenRandomUuid)
        databaseGenerated()
    }
}