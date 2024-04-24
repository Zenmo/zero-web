package com.zenmo.zummon.companysurvey

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import com.zenmo.zummon.UuidSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Address(
    @Serializable(with = UuidSerializer::class)
    val id: Uuid = uuid4(),

    val street: String,
    val houseNumber: Int,
    val houseLetter: String = "", // A-Z allowed
    val houseNumberSuffix: String = "",
    val postalCode: String = "",
    val city: String,

    val gridConnections: List<GridConnection> = emptyList(),
)
