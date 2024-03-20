package com.zenmo.ztor.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            val stackTrace = cause.stackTraceToString()
            call.application.environment.log.error(stackTrace)
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message = mapOf(
                    // have "error" as top-level key so the client can check for it.
                    "error" to mapOf(
                        "type" to cause::class.qualifiedName,
                        "message" to cause.message,
                        // This is truncated somehow
                        "stackTrace" to stackTrace
                    )
                ),
            )
        }
    }
}