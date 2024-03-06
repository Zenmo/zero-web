package com.zenmo.companysurvey.dto

import kotlinx.serialization.Serializable

@Serializable
data class Electricity (
    val hasConnection: Boolean? = null,

    val ean: String = "",

    val authorizationFile: File? = null,
    val quarterHourlyValuesFiles: List<File> = emptyList(),
    val quarterHourlyUsage: List<QuarterHourlyElectricityUsage> = emptyList(),

    // If no kwartierwaarden, ask for annual values.
    val annualElectricityDemandKwh: Int? = null,
    val annualElectricityProductionKwh: Int? = null,

    val kleinverbruik: CompanyKleinverbruik? = null,
    val grootverbruik: CompanyGrootverbruik? = null,
    val gridExpansion: GridExpansion = GridExpansion(),
) {
    fun getConnectionCapacityKw(): Int? {
        return kleinverbruik?.connectionCapacity?.toKw() ?: grootverbruik?.contractedConnectionDemandCapacityKw
    }
}

@Serializable
data class GridExpansion (
    val hasRequestAtGridOperator: Boolean? = null,
    val requestedKW: UInt? = null,
    val reason: String = "",
)

@Serializable
data class CompanyKleinverbruik (
    val connectionCapacity: KleinverbruikElectricityConnectionCapacity? = null,
    // If no kwartierwaarden
    val consumptionProfile: KleinverbruikElectricityConsumptionProfile? = null,
)

@Serializable
data class CompanyGrootverbruik (
    val contractedConnectionDemandCapacityKw: Int? = null,
    val contractedConnectionSupplyCapacityKw: Int? = null,
)

enum class KleinverbruikElectricityConnectionCapacity {
    // The total number of kleinverbruik postal code regions is 359.299.
    // This statistic includes residential connections.

    //`1x6A`, // majority of connections in 89 postalcodes
    //`1x10A`, // majority of connections in 36 postalcodes
    //`1x20A`, // majority of connections in 188 postalcodes
    `1x25A`, // majority of connections in 52.042 postalcodes
    `1x35A`, // majority of connections in 170.736 postalcodes
    `1x40A`, // majority of connections in 6.491 postalcodes
    `1x50A`, // majority of connections in 2.226 postalcodes
    //`1x63A`, // majority of connections in 1 postalcode
    `3x25A`, // majority of connections in 125.261 postalcodes
    `3x35A`, // majority of connections in 1.049 postalcodes
    //`3x40A`, // majority of connections in 87 postalcodes
    `3x50A`, // majority of connections in 180 postalcodes
    `3x63A`, // majority of connections in 261 postalcodes
    `3x80A`, // majority of connections in 613 postalcodes
    ;// Unknown in 39 postalcodes

    fun toKw(): Int {
        return when (this) {
            `1x25A` -> 1 * 25
            `1x35A` -> 1 * 35
            `1x40A` -> 1 * 40
            `1x50A` -> 1 * 50
            `3x25A` -> 3 * 25
            `3x35A` -> 3 * 35
            `3x50A` -> 3 * 50
            `3x63A` -> 3 * 63
            `3x80A` -> 3 * 80
        }
    }
}

// TODO: create profile names
enum class KleinverbruikElectricityConsumptionProfile {
    ONE,
    TWO,
    THREE,
    FOUR,
}

