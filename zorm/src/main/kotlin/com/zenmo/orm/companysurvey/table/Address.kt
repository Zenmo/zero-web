package com.zenmo.orm.companysurvey.table

import com.zenmo.orm.dbutil.ZenmoUUIDTable

/**
 * [com.zenmo.zummon.companysurvey.Address]
 */
object AddressTable: ZenmoUUIDTable("address") {
    val surveyId = uuid("survey_id").references(CompanySurveyTable.id)

    val street = varchar("street", 50)
    val houseNumber = uinteger("house_number")
    val houseLetter = varchar("house_letter", 1)
    val houseNumberSuffix = varchar("house_number_addition", 50)
    val postalCode = varchar("postal_code", 8)
    val city = varchar("city", 50)
}
