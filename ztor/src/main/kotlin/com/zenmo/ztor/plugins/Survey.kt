package com.zenmo.ztor.plugins

import com.zenmo.orm.companysurvey.IndexSurveyRepository
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database

fun Application.configureSurveys(db: Database): Unit {
    val indexSurveyRepository = IndexSurveyRepository(db)

    routing {
        get("/index-surveys") {
            val surveys = indexSurveyRepository.getAllSurveys()

            call.respond(surveys)
        }
    }
}
