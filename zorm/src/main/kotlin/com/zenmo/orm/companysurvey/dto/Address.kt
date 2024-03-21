package com.zenmo.orm.companysurvey.dto

import com.zenmo.orm.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Address(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),

    val street: String,
    val houseNumber: Int,
    val houseLetter: String = "", // A-Z allowed
    val houseNumberSuffix: String = "",
    val postalCode: String = "",
    val city: String,

    val gridConnections: List<GridConnection> = emptyList(),
)
