package com.zenmo.orm.companysurvey

import com.zenmo.orm.companysurvey.table.CompanySurveyTable
import com.zenmo.orm.createSchema
import com.zenmo.orm.connectToPostgres
import com.zenmo.orm.user.table.UserTable
import com.zenmo.zummon.companysurvey.Survey
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.BeforeClass
import java.util.UUID
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
        val survey = Survey(
            companyName = "Zenmo",
            zenmoProject = "Project",
            personName = "John Doe",
            email = "john@example.com",
            addresses = emptyList(),
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
        val survey = createMockSurvey()

        repo.save(survey)
        val storedSurveys = repo.getSurveys(CompanySurveyTable.id eq survey.id)
        assertEquals(1, storedSurveys.size)
        val storedSurvey = storedSurveys.first().copy(
            addresses = survey.addresses.map {
                it.copy(
                    gridConnections = it.gridConnections.map {
                        it.copy(
                            sequence = null,
                        )
                    }
                )
            },
        )

        assertEquals(survey, storedSurvey)
    }

    @Test
    fun testUserCantAccessSurveyInOtherProject() {
        val db = connectToPostgres()
        val userId = UUID.randomUUID()
        transaction(db) {
            UserTable.insert {
                it[id] = userId
                it[projects] = listOf("Middelkaap")
            }
        }

        val repo = SurveyRepository(db)
        repo.save(Survey(
            companyName = "Zenmo",
            zenmoProject = "Middelkaap",
            personName = "John Doe",
            email = "john@example.com",
            addresses = emptyList(),
        ))
        repo.save(Survey(
            companyName = "Zenmo",
            zenmoProject = "Bovenkaap",
            personName = "John Doe",
            email = "john@example.com",
            addresses = emptyList(),
        ))

        val surveys = repo.getSurveysByUser(userId)
        assertEquals(1, surveys.size)
        assertEquals("Middelkaap", surveys[0].zenmoProject)
    }

    @Test
    fun testSequenceIsSameAfterUpsert() {
        val survey = createMockSurvey()
        val db = connectToPostgres()
        val repo = SurveyRepository(db)
        repo.save(survey)

        val storedSurvey1 = repo.getSurveyById(survey.id)!!

        repo.save(storedSurvey1)

        val storedSurvey2 = repo.getSurveyById(storedSurvey1.id)!!

        assertEquals(
            storedSurvey1.addresses.first().gridConnections.first().sequence,
            storedSurvey2.addresses.first().gridConnections.first().sequence,
        )
    }
}
