package com.zenmo.plugins

import com.zenmo.companysurvey.SurveyRepository
import com.zenmo.companysurvey.dto.*
import com.zenmo.errorMessageToJson
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import java.util.*

fun Application.configureDatabases(): Database {
    val db: Database = connectToPostgres()

    routing {
        // Create
        post("/company-survey") {
            val survey: Survey?
            try {
                survey = call.receive<Survey>()
            } catch (e: BadRequestException) {
                if (e.cause is JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest, errorMessageToJson(e.cause?.message))
                    return@post
                }
                call.respond(HttpStatusCode.BadRequest,  errorMessageToJson(e.message))
                return@post
            }

            val repository = SurveyRepository(db)
            repository.save(survey)

            // TODO: return entity from database
            call.respond(HttpStatusCode.Created, survey)
        }
    }

    return db
}

/**
 * @param embedded -- if [true] defaults to an embedded database for tests that runs locally in the same process.
 * In this case you don't have to provide any parameters in configuration file, and you don't have to run a process.
 *
 * @return [Connection] that represent connection to the database. Please, don't forget to close this connection when
 * your application shuts down by calling [Connection.close]
 * */
fun connectToPostgres(): Database {
    Class.forName("org.postgresql.Driver")

    val url = System.getenv("POSTGRES_URL")
    val user = System.getenv("POSTGRES_USER")
    val password = System.getenv("POSTGRES_PASSWORD")

    return Database.connect(url, driver = "org.postgresql.Driver", user, password)
}

fun connectToPostgres(url: String, user: String, password: String): Database =
    Database.connect(url, driver = "org.postgresql.Driver", user, password).also { db -> db.connector }
