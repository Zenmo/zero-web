package com.zenmo.ztor.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.event.*

fun Application.configureMonitoring() {
    install(CallLogging) {
        filter { call -> call.request.path().startsWith("/") }
    }
}
