package com.zenmo.ztor.deeplink

import com.zenmo.orm.deeplink.DeeplinkRepository
import java.util.*
import at.favre.lib.crypto.bcrypt.BCrypt

class DeeplinkService(
    private val deeplinkRepository: DeeplinkRepository
) {
    fun generateDeeplink(surveyId: UUID): Deeplink {
        val secret = generateSecret()
        val hash = BCrypt.withDefaults().hashToString(8, secret.toCharArray())

        val deeplinkId = deeplinkRepository.saveDeeplink(surveyId, hash)

        return Deeplink(deeplinkId, surveyId, secret)
    }

    fun assertValidDeeplink(surveyId: UUID, deeplinkId: UUID, secret: String) {
        val deeplink = deeplinkRepository.getDeeplinkById(deeplinkId)
        if (deeplink == null) {
            throw Exception("Deeplink does not exist")
        }

        if (deeplink.surveyId != surveyId) {
            throw Exception("Deeplink does not belong to this survey")
        }

        if (!BCrypt.verifyer().verify(secret.toCharArray(), deeplink.bcryptSecret).verified) {
            throw Exception("Invalid deeplink secret")
        }
    }
}

fun generateSecret(): String {
    val alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    return (1..16)
        .map { alphabet.random() }
        .joinToString("")
}