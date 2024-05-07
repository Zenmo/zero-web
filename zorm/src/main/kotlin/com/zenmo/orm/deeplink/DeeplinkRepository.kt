package com.zenmo.orm.deeplink

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insertReturning
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class DeeplinkRepository(
    private val db: Database
) {
    fun getDeeplinkById(id: UUID): Deeplink? =
        transaction(db) {
            DeeplinkTable.selectAll().where { DeeplinkTable.id eq id }.firstOrNull()?.let {
                Deeplink(
                    id = it[DeeplinkTable.id],
                    surveyId = it[DeeplinkTable.surveyId],
                    created = it[DeeplinkTable.created],
                    bcryptSecret = it[DeeplinkTable.bcryptSecret]
                )
            }
        }

    fun saveDeeplink(surveyId: UUID, bcryptSecret: String): UUID =
        transaction(db) {
            DeeplinkTable.insertReturning {
                it[DeeplinkTable.surveyId] = surveyId
                it[DeeplinkTable.bcryptSecret] = bcryptSecret
            }.map {
                it[DeeplinkTable.id]
            }.single()
        }
}