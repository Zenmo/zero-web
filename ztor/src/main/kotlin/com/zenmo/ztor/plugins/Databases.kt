package com.zenmo.ztor.plugins

import com.zenmo.orm.companysurvey.SurveyRepository
import com.zenmo.orm.companysurvey.dto.*
import com.zenmo.orm.connectToPostgres
import com.zenmo.ztor.errorMessageToJson
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.Database
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
