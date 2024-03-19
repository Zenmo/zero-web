package com.zenmo.companysurvey.dto

import com.zenmo.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Address(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),

    val street: String,
    val houseNumber: UInt,
    val houseLetter: String = "", // A-Z allowed
    val houseNumberSuffix: String = "",
    val postalCode: String = "",
    val city: String,

    val gridConnections: List<GridConnection> = emptyList(),
)