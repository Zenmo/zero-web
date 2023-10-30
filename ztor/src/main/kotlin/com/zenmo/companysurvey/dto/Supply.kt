package com.zenmo.companysurvey.dto

import kotlinx.serialization.Serializable

@Serializable
data class Supply (
    val hasSupply: Boolean?,

    val pvInstalledKwp: Int?,
    val pvOrientation: PVOrientation?,

    val pvPlanned: Boolean?,
    val pvPlannedCapacityKwp: Int?,
    val pvPlannedYear: Int?,

    val windInstalledKw: Float?,
    val otherSupply: String,
)

enum class PVOrientation {
    SOUTH,
    EASTWEST,
    OTHER,
    UNKNOWN,
}
