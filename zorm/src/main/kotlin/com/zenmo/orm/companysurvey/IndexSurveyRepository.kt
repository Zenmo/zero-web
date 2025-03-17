package com.zenmo.orm.companysurvey

import com.zenmo.joshi.IndexSurvey
import com.zenmo.orm.companysurvey.table.CompanySurveyTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid

class IndexSurveyRepository(
    val db: Database
) {
    fun getAllSurveys(
        userId: Uuid,
    ): List<IndexSurvey> {
        return transaction(db) {
            CompanySurveyTable.selectAll()
                .where {
                    userIsAllowedCondition(userId.toJavaUuid())
                }
                .map {
                IndexSurvey(
                    it[CompanySurveyTable.id].toKotlinUuid(),
                    it[CompanySurveyTable.companyName],
                )
            }
        }
    }
}
