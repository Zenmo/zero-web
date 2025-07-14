package com.zenmo.zummon.companysurvey

import kotlinx.serialization.Serializable

@Serializable
data class Heat (
    val heatingTypes: List<HeatingType> = emptyList(),

    val sumGasBoilerKw: Float? = null,
    val sumHeatPumpKw: Float? = null,
    // Only the electricity component of hybrid setup
    val sumHybridHeatPumpElectricKw: Float? = null,

    val annualDistrictHeatingDelivery_GJ: Float? = null,

    val localHeatExchangeDescription: String = "",
    val hasUnusedResidualHeat: Boolean? = null,

    val heatPumpElectricityConsumptionTimeSeries_kWh: TimeSeries? = null,
    val heatPumpHeatProductionTimeSeries_kWh: TimeSeries? = null,
) {
    @Deprecated("Renamed to annualDistrictHeatingDelivery_GJ", ReplaceWith("annualDistrictHeatingDelivery_GJ"))
    val annualDistrictHeatingDemandGj
        get() = annualDistrictHeatingDelivery_GJ

    fun allTimeSeries() = listOfNotNull(
        heatPumpElectricityConsumptionTimeSeries_kWh,
        heatPumpHeatProductionTimeSeries_kWh,
    )
}

enum class HeatingType {
    GAS_BOILER,
    ELECTRIC_HEATPUMP,
    HYBRID_HEATPUMP,
    DISTRICT_HEATING,
    // Warmte-krachtkoppeling (WKK)
    COMBINED_HEAT_AND_POWER,
    OTHER,
}
