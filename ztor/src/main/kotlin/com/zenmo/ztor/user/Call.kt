package com.zenmo.ztor.user

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.sessions.*
import java.util.UUID

/**
 * Get the current user ID from the session cookie or the Authorization header.
 */
fun ApplicationCall.getUserId(): UUID? {
    val userSession = this.sessions.get<UserSession>()
    if (userSession != null) {
        // User is logged in via cookie
        return userSession.getUserId()
    }

    val authorizationHeader = this.request.authorization()
    if (authorizationHeader == null) {
        // not logged in
        return null
    }

    if (!authorizationHeader.startsWith("Bearer ")) {
        throw Exception("Authorization header is not a Bearer token")
    }

    val accessToken = authorizationHeader.removePrefix("Bearer ")
    val payload = validateAccessTokenAndGetPayload(accessToken)
    return decodePayload(payload).sub
}
