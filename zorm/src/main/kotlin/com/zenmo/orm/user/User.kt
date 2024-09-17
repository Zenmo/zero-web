package com.zenmo.orm.user

data class User(
    // Keycloak id
    val id: String,
    val projects: List<String>,
    val note: String,
)
