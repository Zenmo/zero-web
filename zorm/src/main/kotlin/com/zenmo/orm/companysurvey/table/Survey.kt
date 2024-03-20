package com.zenmo.orm.companysurvey.table

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

/**
 * [com.zenmo.companysurvey.dto.Survey]
 */
object CompanySurveyTable: Table("company_survey") {
    val id = uuid("id").autoGenerate()
    override val primaryKey = PrimaryKey(id)
    val created = timestamp("created_at").defaultExpression(CurrentTimestamp())
    // Can be fetched at https://energiekeregio.nl/api/v1/zenmo?details=15989
    val energiekeRegioId = uinteger("energieke_regio_id").nullable()
    // Zenmo project name
    val project = varchar("project", 50)

    val companyName = varchar("company_name", 50)
    val personName = varchar("person_name", 50)
    val email = varchar("email", 50)
    val dataSharingAgreed = bool("data_sharing_agreed").default(false)
}
