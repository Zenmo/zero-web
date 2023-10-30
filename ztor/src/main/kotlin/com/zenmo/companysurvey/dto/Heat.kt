package com.zenmo.companysurvey.dto

import kotlinx.serialization.Serializable

@Serializable
data class Heat (
    val heatingTypes: List<HeatingType>,

    val combinedGasBoilerKw: Float?,
    val combinedHeatPumpKw: Float?,
    // Does this mean only the electricity component?
    // Suggestion: make it a checkbox "uses hybrid setup"
    val combinedHybridHeatPumpKw: Float?,

    val annualDistrictHeatingDemandGj: Float?,

    val localHeatExchangeDescription: String,
)

enum class HeatingType {
    GAS_BOILER,
    ELECTRIC_HEATPUMP,
    HYBRID_HEATPUMP,
    DISTRICT_HEATING,
}
