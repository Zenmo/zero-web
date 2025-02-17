package com.zenmo.ztor.plugins

import com.zenmo.orm.companysurvey.ProjectRepository
import com.zenmo.orm.companysurvey.SurveyRepository
import com.zenmo.orm.connectToPostgres
import com.zenmo.orm.deeplink.DeeplinkRepository
import com.zenmo.orm.user.UserRepository
import com.zenmo.ztor.deeplink.DeeplinkService
import com.zenmo.ztor.errorMessageToJson
import com.zenmo.ztor.user.getUserId
import com.zenmo.zummon.companysurvey.Survey
import com.zenmo.zummon.companysurvey.Project
import com.zenmo.zummon.User
import com.zenmo.zummon.usersFromJson

import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import java.util.*

fun Application.configureDatabases(): Database {
    val db: Database = connectToPostgres()
    val userRepository = UserRepository(db)
    val surveyRepository = SurveyRepository(db)
    val projectRepository = ProjectRepository(db)
    val deeplinkService = DeeplinkService(DeeplinkRepository(db))

    routing {
        suspend fun RoutingContext.asAdmin(body: suspend () -> Unit) {
            val userId = call.getUserId()

            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized, "User not logged in")
                return 
            }

            if (!userRepository.isAdmin(userId)) {
                call.respond(HttpStatusCode.Forbidden, "User is not admin")
                return 
            }

            body()
         }

        get("/users") {
            asAdmin {
                val users = userRepository.getUsersAndProjects()
                call.respond(HttpStatusCode.OK, users)
            }
        }

        get("/users/{userId}") {
            asAdmin {
                val userId = UUID.fromString(call.parameters["userId"])
                val user = userRepository.getUserById(userId)
                call.respond(HttpStatusCode.OK, user)
            }
        }

        get("/users/{userId}/projects") {
            asAdmin {
                val userId = UUID.fromString(call.parameters["userId"])
                val user = userRepository.getUserAndProjects(userId)
                call.respond(HttpStatusCode.OK, user)
            }
        }

        // Create
        post("/users") {
            val user: User?
            try {
                user = call.receive<User>()
            } catch (e: BadRequestException) {
                if (e.cause is JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest, errorMessageToJson(e.cause?.message))
                    return@post
                }
                call.respond(HttpStatusCode.BadRequest,  errorMessageToJson(e.message))
                return@post
            }
            asAdmin {
                val newUser = userRepository.save(user)
                call.respond(HttpStatusCode.Created, newUser)
            }
        }

        // Update
        put("/users") {
            val user: User?
            try {
                user = call.receive<User>()
            } catch (e: BadRequestException) {
                if (e.cause is JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest, errorMessageToJson(e.cause?.message))
                    return@put
                }
                call.respond(HttpStatusCode.BadRequest,  errorMessageToJson(e.message))
                return@put
            }
            asAdmin {
                val newUser = userRepository.save(user)
                call.respond(HttpStatusCode.OK, newUser)
            }
        }

        delete("/users/{userId}") {
            asAdmin {
                val userId = UUID.fromString(call.parameters["userId"])
                userRepository.deleteUserById(userId)
                call.respond(HttpStatusCode.OK)
            }
        }

        get("/all-projects") {
            asAdmin {
                call.respond(HttpStatusCode.OK, projectRepository.getProjects())
            }
        }

        get("/projects") {
            val userId = call.getUserId()
            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            call.respond(HttpStatusCode.OK, projectRepository.getProjectsByUserId(userId))
        }

        get("/projects/{projectId}") {
            val projectId = UUID.fromString(call.parameters["projectId"])

            val userId = call.getUserId()
            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            call.respond(HttpStatusCode.OK, projectRepository.getProjectByUserId(userId, projectId))
        }

        // Create
        post("/projects") {
            val project: Project?
            try {
                project = call.receive<Project>()
            } catch (e: BadRequestException) {
                if (e.cause is JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest, errorMessageToJson(e.cause?.message))
                    return@post
                }
                call.respond(HttpStatusCode.BadRequest,  errorMessageToJson(e.message))
                return@post
            }

            val userId = call.getUserId()
            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }

            val newProject = projectRepository.saveToUser(project, userId)

            call.respond(HttpStatusCode.Created, newProject)
        }

        // Update
        put("/projects") {
            val project: Project?
            try {
                project = call.receive<Project>()
            } catch (e: BadRequestException) {
                if (e.cause is JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest, errorMessageToJson(e.cause?.message))
                    return@put
                }
                call.respond(HttpStatusCode.BadRequest,  errorMessageToJson(e.message))
                return@put
            }

            val newProject = projectRepository.save(project)

            call.respond(HttpStatusCode.OK, newProject)
        }

        get("/projects/by-name/{projectName}/buurtcodes") {
            val projectName = call.parameters["projectName"]!!
            call.respond(HttpStatusCode.OK, projectRepository.getBuurtCodesByProjectName(projectName))
        }

        // Create
        post("/company-surveys") {
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

            val surveyId = surveyRepository.save(survey, call.getUserId())

            val deeplink = deeplinkService.generateDeeplink(surveyId)

            call.respond(HttpStatusCode.Created, deeplink)
        }

        // fetch all
        get("/company-surveys") {
            val userId = call.getUserId()
            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            val repository = SurveyRepository(db)

            val includeInSimulation = call.request.queryParameters["includeInSimulation"]?.toBoolean()
            val project = call.request.queryParameters["project"]
            val projectNames = call.request.queryParameters["projectNames"]?.split(",")

            val surveys = repository.getSurveys(
                userId = userId,
                project = project,
                includeInSimulation = includeInSimulation,
                projectNames = projectNames,
            )

            call.respond(HttpStatusCode.OK, surveys)
        }

        // fetch single
        get("/company-surveys/{surveyId}") {
            val surveyId = UUID.fromString(call.parameters["surveyId"])

            val deeplink = call.request.queryParameters.get("deeplink")
            val secret = call.request.queryParameters.get("secret")

            if (deeplink != null && secret != null) {
                val deeplinkId = UUID.fromString(deeplink)
                deeplinkService.assertValidDeeplink(surveyId, deeplinkId, secret)
                val survey = surveyRepository.getSurveyById(surveyId)
                if (survey == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(HttpStatusCode.OK, survey)
            }

            val userId = call.getUserId()
            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            val survey = surveyRepository.getSurveyByIdWithUserAccessCheck(surveyId, userId)
            if (survey == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.respond(HttpStatusCode.OK, survey)
        }

        delete("/company-surveys/{surveyId}") {
            val userId = call.getUserId()
            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@delete
            }

            val surveyId = UUID.fromString(call.parameters["surveyId"])

            surveyRepository.deleteSurveyById(surveyId, userId)

            call.respond(HttpStatusCode.OK)
        }

        // generate deeplink
        post("/company-surveys/{surveyId}/deeplink") {
            val userId = call.getUserId()
            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }

            val surveyId = UUID.fromString(call.parameters["surveyId"])

            val survey = surveyRepository.getSurveyByIdWithUserAccessCheck(surveyId, userId)
            if (survey == null) {
                // User may not have access to this project
                call.respond(HttpStatusCode.NotFound)
                return@post
            }

            val deeplink = deeplinkService.generateDeeplink(surveyId)

            call.respond(HttpStatusCode.Created, deeplink)
        }

        // set active state
        put("/company-surveys/{surveyId}/include-in-simulation") {
            val userId = call.getUserId()
            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@put
            }

            val surveyId = UUID.fromString(call.parameters["surveyId"])
            val includeInSimulation = call.receive<Boolean>()
            surveyRepository.setIncludeInSimulation(surveyId, userId, includeInSimulation)

            call.respond(HttpStatusCode.OK)
        }
    }
    return db
}
