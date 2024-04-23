package com.zenmo.ztor

import com.zenmo.orm.connectToPostgres
import com.zenmo.orm.createSchema
import com.zenmo.orm.echoSchemaSql
import com.zenmo.ztor.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.count() == 1) {
        if (args[0] == "create-schema") {
            println("Creating database schema...")
            val db = connectToPostgres()
            createSchema(db)
            println("Schema created!")
            return
        }

        if (args[0] == "echo-schema-sql") {
            val db = connectToPostgres()
            echoSchemaSql(db)
            return
        }

        println("Unknown argument: ${args[0]}")
        exitProcess(2) // 2 means argument error
    }

    if (args.count() > 1) {
        println("Too many arguments")
        exitProcess(2) // 2 means argument error
    }

    embeddedServer(Netty, port = 8082, host = "0.0.0.0", module = Application::module)
            .start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureAuthentication()
    configureDatabases()
    configureRouting()
    configureStatusPages()
    configureUpload()
}
