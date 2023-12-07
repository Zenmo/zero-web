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

    val windInstalledKw: Float? = null,
    val otherSupply: String = "",
)

enum class PVOrientation {
    SOUTH,
    EAST_WEST,
}
