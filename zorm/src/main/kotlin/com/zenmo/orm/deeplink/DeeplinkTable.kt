package com.zenmo.orm.deeplink

import com.zenmo.orm.companysurvey.table.CompanySurveyTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object DeeplinkTable: Table("survey_deeplink") {
    val id = uuid("id").autoGenerate()
    override val primaryKey = PrimaryKey(id)

    val surveyId = uuid("survey_id").references(CompanySurveyTable.id, onDelete = ReferenceOption.CASCADE)
    val created = timestamp("created").defaultExpression(CurrentTimestamp)
//    val expires = timestamp("expires").nullable().default(null)
    val bcryptSecret = varchar("bcrypt_secret", 1000)
}
