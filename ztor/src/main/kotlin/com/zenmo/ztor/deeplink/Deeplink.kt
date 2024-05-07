package com.zenmo.ztor.deeplink

import com.zenmo.orm.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Deeplink(
    @Serializable(with = UUIDSerializer::class)
    val deeplinkId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val surveyId: UUID,
    val secret: String,
)
