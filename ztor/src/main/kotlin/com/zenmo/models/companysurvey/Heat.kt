package com.zenmo.models.companysurvey

import kotlinx.serialization.Serializable

@Serializable
data class Heat (
    val heatingType: HeatingType?,
    val naturalGas: NaturalGas,
    val districtHeating: DistrictHeating,
)

enum class HeatingType {
    HEATPUMP,
    NATURAL_GAS,
    ELECTRIC_BOILER,
}

@Serializable
data class NaturalGas (
    val hasConnection: Boolean?,
    val annualDemandM3: Int?,
    val hourlyProfile: List<File>,
    val percentageUsedHeating: Int?,
)

@Serializable
data class DistrictHeating (
    val hasConnection: Boolean?,
    val annualDemandGj: Double?,
)
