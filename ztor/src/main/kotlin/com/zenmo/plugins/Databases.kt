package com.zenmo.plugins

import com.zenmo.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

fun Application.configureDatabases() {
    val db: Database = connectToPostgres(embedded = false)

    transaction(db) {
        SchemaUtils.create(CompanySurveyTable, CompanySurveyElectricityConnectionTable)
    }

    routing {
        // Create
        post("/company-survey") {
            val survey = call.receive<CompanySurvey>()

            transaction(db) {
                val surveyId = UUID.randomUUID()
                CompanySurveyTable.insert {
                    it[id] = surveyId
                    it[companyName] = survey.companyName
                    it[personName] = survey.personName
                    it[email] = survey.email
                    it[usageAssets] = survey.usageAssets
                    it[generationAssets] = survey.generationAssets
                    it[usagePattern] = survey.usagePattern
                }

                CompanySurveyElectricityConnectionTable.batchInsert(survey.electricityConnections) {
                    companyElectricityConnection ->
                        this[CompanySurveyElectricityConnectionTable.surveyId] = surveyId
                        this[CompanySurveyElectricityConnectionTable.street] = companyElectricityConnection.street
                        this[CompanySurveyElectricityConnectionTable.houseNumber] = companyElectricityConnection.houseNumber
                        this[CompanySurveyElectricityConnectionTable.houseLetter] = companyElectricityConnection.houseLetter
                        this[CompanySurveyElectricityConnectionTable.houseNumberSuffix] = companyElectricityConnection.houseNumberSuffix
                        this[CompanySurveyElectricityConnectionTable.annualUsageKWh] = companyElectricityConnection.annualUsageKWh
                        this[CompanySurveyElectricityConnectionTable.quarterlyValuesFile] = companyElectricityConnection.quarterlyValuesFile
                        this[CompanySurveyElectricityConnectionTable.ean] = companyElectricityConnection.ean
                        this[CompanySurveyElectricityConnectionTable.description] = companyElectricityConnection.description
                }
            }

            // TODO: return entity from database
            call.respond(HttpStatusCode.Created, survey)
        }
    }
}

/**
 * @param embedded -- if [true] defaults to an embedded database for tests that runs locally in the same process.
 * In this case you don't have to provide any parameters in configuration file, and you don't have to run a process.
 *
 * @return [Connection] that represent connection to the database. Please, don't forget to close this connection when
 * your application shuts down by calling [Connection.close]
 * */
fun Application.connectToPostgres(embedded: Boolean): Database {
    Class.forName("org.postgresql.Driver")
    return if (embedded) {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver", user = "root", password = "")
    } else {
        val url = System.getenv("POSTGRES_URL")
        val user = System.getenv("POSTGRES_USER")
        val password = System.getenv("POSTGRES_PASSWORD")

        Database.connect(url, driver = "org.postgresql.Driver", user, password)
    }
}
