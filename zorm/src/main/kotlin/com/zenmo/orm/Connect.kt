package com.zenmo.orm

import org.jetbrains.exposed.sql.Database

fun connectToPostgres(): Database {
    val url = System.getenv("POSTGRES_URL")
    val user = System.getenv("POSTGRES_USER")
    val password = System.getenv("POSTGRES_PASSWORD")

    return Database.connect(url, driver = org.postgresql.Driver::class.qualifiedName!!, user, password)
}

fun connectToPostgres(url: String, user: String, password: String): Database =
    Database.connect(url, driver = org.postgresql.Driver::class.qualifiedName!!, user, password).also { db -> db.connector }
