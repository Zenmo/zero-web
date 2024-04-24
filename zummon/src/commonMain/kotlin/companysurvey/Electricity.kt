package com.zenmo.zummon.companysurvey
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import com.zenmo.zummon.companysurvey.KleinverbruikElectricityConnectionCapacity.valueOf
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

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
    fun getHasConnection(): Boolean {
        return hasConnection ?: false
    }
    
    fun getContractedConnectionCapacityKw(): Int? {
        return kleinverbruik?.connectionCapacity?.toKw() ?: grootverbruik?.contractedConnectionDemandCapacityKw
    }
}

@Serializable
data class GridExpansion (
    val hasRequestAtGridOperator: Boolean? = null,
    val requestedKW: Int? = null,
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
    val physicalCapacityKw: Int? = null,
)

enum class KleinverbruikElectricityConnectionCapacity {
    // The total number of kleinverbruik postal code regions is 359.299.
    // This statistic includes residential connections.

    //_1x6A, // majority of connections in 89 postalcodes
    //_1x10A, // majority of connections in 36 postalcodes
    //_1x20A, // majority of connections in 188 postalcodes
    _1x25A, // majority of connections in 52.042 postalcodes
    _1x35A, // majority of connections in 170.736 postalcodes
    _1x40A, // majority of connections in 6.491 postalcodes
    _1x50A, // majority of connections in 2.226 postalcodes
    //_1x63A, // majority of connections in 1 postalcode
    _3x25A, // majority of connections in 125.261 postalcodes
    _3x35A, // majority of connections in 1.049 postalcodes
    //_3x40A, // majority of connections in 87 postalcodes
    _3x50A, // majority of connections in 180 postalcodes
    _3x63A, // majority of connections in 261 postalcodes
    _3x80A, // majority of connections in 613 postalcodes
    ;// Unknown in 39 postalcodes

    fun toKw(): Int {
        return when (this) {
            _1x25A -> 1 * 25
            _1x35A -> 1 * 35
            _1x40A -> 1 * 40
            _1x50A -> 1 * 50
            _3x25A -> 3 * 25
            _3x35A -> 3 * 35
            _3x50A -> 3 * 50
            _3x63A -> 3 * 63
            _3x80A -> 3 * 80
        }
    }

    fun toDisplayName(): String =
        this.name.substring(1)
}

fun kleinverbruikEnumFromDisplayName(displayName: String): KleinverbruikElectricityConnectionCapacity =
    valueOf("_$displayName")

// TODO: create profile names
enum class KleinverbruikElectricityConsumptionProfile {
    ONE,
    TWO,
    THREE,
    FOUR,
}
