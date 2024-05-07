package com.zenmo.orm.deeplink

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.UUID

data class Deeplink(
    val id: UUID = UUID.randomUUID(),
    val surveyId: UUID,
    val created: Instant = Clock.System.now(),
    val bcryptSecret: String,
)
