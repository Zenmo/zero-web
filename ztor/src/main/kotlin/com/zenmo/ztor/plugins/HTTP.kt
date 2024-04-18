package com.zenmo.ztor.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowOrigins {
            it.matches(
                Regex(
                    // e.g. "https://gray-hill-0e1d72903-\\d+.westeurope.3.azurestaticapps.net"
                    System.getenv("CORS_ALLOW_ORIGIN_PATTERN")
                )
            )
        }
        allowCredentials = true
    }
}
