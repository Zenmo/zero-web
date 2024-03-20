package com.zenmo.orm.companysurvey.dto

import kotlinx.serialization.Serializable

@Serializable
data class Heat (
    val heatingTypes: List<HeatingType> = emptyList(),

    val sumGasBoilerKw: Float? = null,
    val sumHeatPumpKw: Float? = null,
    // Only the electricity component of hybrid setup
    val sumHybridHeatPumpElectricKw: Float? = null,

    val annualDistrictHeatingDemandGj: Float? = null,

    val localHeatExchangeDescription: String = "",
    val hasUnusedResidualHeat: Boolean? = null,
)

enum class HeatingType {
    GAS_BOILER,
    ELECTRIC_HEATPUMP,
    HYBRID_HEATPUMP,
    DISTRICT_HEATING,
    OTHER,
}
