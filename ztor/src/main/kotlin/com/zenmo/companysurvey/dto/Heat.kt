package com.zenmo.companysurvey.dto

import kotlinx.serialization.Serializable

@Serializable
data class Heat (
    val heatingTypes: List<HeatingType>,

    val sumGasBoilerKw: Float?,
    val sumHeatPumpKw: Float?,
    // Only the electricity component of hybrid setup
    val sumHybridHeatPumpElectricKw: Float?,

    val annualDistrictHeatingDemandGj: Float?,

    val localHeatExchangeDescription: String,
    val hasUnusedResidualHeat: Boolean?,
)

enum class HeatingType {
    GAS_BOILER,
    ELECTRIC_HEATPUMP,
    HYBRID_HEATPUMP,
    DISTRICT_HEATING,
    OTHER,
}
