package com.zenmo.companysurvey.table

import org.jetbrains.exposed.sql.Table

/**
 * [com.zenmo.companysurvey.dto.Address]
 */
object AddressTable: Table("address") {
    val id = uuid("id").autoGenerate()
    override val primaryKey = PrimaryKey(id)

    val surveyId = uuid("survey_id").references(CompanySurveyTable.id)

    val street = varchar("street", 50)
    val houseNumber = uinteger("house_number")
    val houseLetter = varchar("house_letter", 1)
    val houseNumberSuffix = varchar("house_number_addition", 50)
    val postalCode = varchar("postal_code", 8)
    val city = varchar("city", 50)
}
