package com.zenmo.companysurvey.dto

import kotlinx.serialization.Serializable

@Serializable
data class Supply (
    val hasSupply: Boolean? = null,

    val pvInstalledKwp: Int? = null,
    val pvOrientation: PVOrientation? = null,

    val pvPlanned: Boolean? = null,
    val pvPlannedKwp: Int? = null,
    val pvPlannedOrientation: PVOrientation? = null,
    val pvPlannedYear: Int? = null,

    val missingPvReason: MissingPvReason? = null,

    val windInstalledKw: Float? = null,
    val otherSupply: String = "",
)

enum class PVOrientation {
    SOUTH,
    EAST_WEST,
}

enum class MissingPvReason {
    NO_SUITABLE_ROOF,
    NO_BACKFEED_CAPACITY,
    NOT_INTERESTED,
    OTHER,
}
