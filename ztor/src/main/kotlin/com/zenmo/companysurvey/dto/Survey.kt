package com.zenmo.companysurvey.dto

import com.zenmo.UUIDSerializer
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Root object
 */
@Serializable
data class Survey(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    val created: Instant = Clock.System.now(),
    val zenmoProject: String,
    val companyName: String,
    val personName: String,
    val email: String = "",
    val dataSharingAgreed: Boolean = false,

    val addresses: List<Address>,
)

