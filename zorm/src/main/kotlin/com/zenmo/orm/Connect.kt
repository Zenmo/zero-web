package com.zenmo.orm

import org.jetbrains.exposed.sql.Database

fun connectToPostgres(): Database {
    val url = getenv("POSTGRES_URL")
    val user = getenv("POSTGRES_USER")
    val password = getenv("POSTGRES_PASSWORD")

    return connectToPostgres(url, user, password)
}

fun connectToPostgres(url: String, user: String, password: String): Database =
    Database.connect(url, driver = org.postgresql.Driver::class.qualifiedName!!, user, password)

fun getenv(name: String): String {
    val value = System.getenv(name) ?: throw Exception("Set the $name environment variable")
    return value
}
