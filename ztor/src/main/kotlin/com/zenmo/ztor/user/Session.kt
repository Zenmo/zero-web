package com.zenmo.ztor.user

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val state: String,
    val token: String,
) {
    /**
     * Contains user info
     */
    fun getDecodedAccessToken() = decodeAccessToken(token)

    fun getUserId() = getDecodedAccessToken().sub
}
