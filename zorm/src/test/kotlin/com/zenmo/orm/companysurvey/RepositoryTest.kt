package com.zenmo.orm.companysurvey

import com.zenmo.orm.companysurvey.dto.*
import com.zenmo.orm.companysurvey.table.CompanySurveyTable
import com.zenmo.orm.createSchema
import com.zenmo.orm.connectToPostgres
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.BeforeClass
import kotlin.test.Test
import kotlin.test.assertEquals

class RepositoryTest {
    companion object {
        /**
         * Drop and create database before running tests.
         */
        @JvmStatic
        @BeforeClass
        fun setupClass() {
            val db = connectToPostgres()
            val schema = Schema(db.connector().schema)
            transaction(db) {
                SchemaUtils.dropSchema(schema, cascade = true)
                SchemaUtils.createSchema(schema)
            }

            createSchema(db)
        }
    }

    @Test
    fun testSaveMinimalSurvey() {
        val db = connectToPostgres()
        val repo = SurveyRepository(db)
        var survey = Survey(
            companyName = "Zenmo",
            zenmoProject = "Project",
            personName = "John Doe",
            email = "john@example.com",
            addresses = emptyList(),
        )
        survey = survey.copy(
            created = roundInstant(survey.created)
        )
        repo.save(survey)
        val storedSurveys = repo.getSurveys(CompanySurveyTable.id eq survey.id)
        assertEquals(1, storedSurveys.size)
        assertEquals(survey, storedSurveys.first())
    }

    @Test
    fun testSaveWithGridConnections() {
        val db = connectToPostgres()
        val repo = SurveyRepository(db)
        val survey = mockSurvey.copy(
            created = roundInstant(mockSurvey.created)
        )

        repo.save(survey)
        val storedSurveys = repo.getSurveys(CompanySurveyTable.id eq survey.id)
        assertEquals(1, storedSurveys.size)

        val expectedSurvey = survey.copy(
            addresses = survey.addresses.map {
                it.copy(
                    gridConnections = it.gridConnections.map {
                        it.copy(
                            sequence = 1,
                        )
                    }
                )
             },
        )
        assertEquals(expectedSurvey, storedSurveys.first())
    }

    /**
     * Round so we can compare with some loss of precision.
     */
    fun roundInstant(instant: Instant): Instant {
        return Instant.fromEpochMilliseconds(instant.toEpochMilliseconds())
    }
}