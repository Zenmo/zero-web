package com.zenmo.fudura

import kotlinx.serialization.Serializable

@Serializable
data class GetTelemetryResult(
    /**
     * Comes in batches of 1000
     */
    val telemetry: List<Telemetry>,
    val continuationToken: String? = null,
)

@Serializable
data class Telemetry(
    val value: String,
    val readingTimestamp: String,
    val tariff: String? = null, // Low or Normal
    val isValid: Boolean? = null,
    val repairStatus: String? = null,
)
