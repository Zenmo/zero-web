package com.zenmo.companysurvey.dto

import kotlinx.serialization.Serializable

@Serializable
data class Electricity (
    val ean: String = "",

    val quarterHourlyValuesFiles: List<File> = emptyList(),
    val quarterHourlyUsage: List<QuarterHourlyElectricityUsage> = emptyList(),

    // If no kwartierwaarden, ask for annual values.
    val annualElectricityDemandKwh: Int? = null,
    val annualElectricityProductionKwh: Int? = null,

    val kleinverbruik: CompanyKleinverbruik? = null,
    val grootverbruik: CompanyGrootverbruik? = null,
)

@Serializable
data class CompanyKleinverbruik (
    val connectionCapacity: KleinverbruikElectricityConnectionCapacity? = null,
    // If no kwartierwaarden
    val consumptionProfile: KleinverbruikElectricityConsumptionProfile? = null,
)

@Serializable
data class CompanyGrootverbruik (
    val contractedConnectionDemandCapacityKw: Int?,
    val contractedConnectionSupplyCapacityKw: Int?,
)

enum class KleinverbruikElectricityConnectionCapacity {
    // The total number of kleinverbruik postal code regions is 359.299.
    // This statistic includes residential connections.
    `1x40A`, // majority of connections in 6.491 postalcodes
    `1x50A`, // majority of connections in 2.226 postalcodes
    `3x25A`, // majority of connections in 125.261 postalcodes
    `3x35A`, // majority of connections in 1.049 postalcodes
    `3x50A`, // majority of connections in 180 postalcodes
    `3x63A`, // majority of connections in 261 postalcodes
    `3x80A`, // majority of connections in 613 postalcodes
}

// TODO: create profile names
enum class KleinverbruikElectricityConsumptionProfile {
    ONE,
    TWO,
    THREE,
    FOUR,
}

