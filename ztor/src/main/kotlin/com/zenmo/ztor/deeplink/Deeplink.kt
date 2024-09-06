package com.zenmo.ztor.deeplink

import com.zenmo.ztor.user.JavaUUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Deeplink(
    @Serializable(with = JavaUUIDSerializer::class)
    val deeplinkId: UUID,
    @Serializable(with = JavaUUIDSerializer::class)
    val surveyId: UUID,
    val secret: String,
)
