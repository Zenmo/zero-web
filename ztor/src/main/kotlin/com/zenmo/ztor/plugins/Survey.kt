package com.zenmo.ztor.plugins

import com.zenmo.orm.companysurvey.IndexSurveyRepository
import com.zenmo.ztor.user.getUserId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import kotlin.uuid.toKotlinUuid

fun Application.configureSurveys(db: Database): Unit {
    val indexSurveyRepository = IndexSurveyRepository(db)

    routing {
        get("/index-surveys") {
            when (val signedInUser = call.getUserId()) {
                null -> call.respond(HttpStatusCode.Unauthorized)
                else -> {
                    val surveys = indexSurveyRepository.getAllSurveys(signedInUser.toKotlinUuid())
                    call.respond(surveys)
                }
            }
        }
    }
}
