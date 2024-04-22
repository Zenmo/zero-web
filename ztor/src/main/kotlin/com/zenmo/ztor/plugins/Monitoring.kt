package com.zenmo.ztor.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.event.*

fun Application.configureMonitoring() {
    install(CallLogging)
    {
 //Uncomment to log more
        format {
            extendedFormat(it)
        }
    }
}

fun extendedFormat(it: ApplicationCall): String = buildString {
    append(it.request.httpMethod.value)
    append(" ")
    append(it.request.path())
    appendLine()
    addHeaders(it.request.headers)
    appendLine()

    append(it.response.status())
    addHeaders(it.response.headers.allValues())
}

fun StringBuilder.addHeaders(headers: Headers) {
    headers.forEach { key, values ->
        values.forEach {
            append("  ")
            append(key)
            append(": ")
            append(it)
            appendLine()
        }
    }
}
