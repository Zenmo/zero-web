package com.zenmo.orm.companysurvey.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.VarCharColumnType

object ProjectTable: Table("project") {
    val id = uuid("id").autoGenerate()
    override val primaryKey = PrimaryKey(id)

    val name = varchar("name", 50).uniqueIndex()
    val energiekeRegioId = integer("energieke_regio_id").uniqueIndex().nullable()
    // Maybe this is sufficient for now.
    // We may want to introduce a geometry field.
    val buurtCodes = array<String>("buurt_codes", VarCharColumnType(50)).default(emptyList())
}
