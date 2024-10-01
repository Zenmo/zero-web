package com.zenmo.fudura

import kotlinx.serialization.Serializable

@Serializable
data class GetChannelMetadataResult(
    val channelId: String,
    val channelType: ChannelType,
    val channelDataType: ChannelDataType,
    val productType: ProductType,
    val description: String,
    val longDescription: String,
    // "00:15:00" for quarter-hourly data.
    // not present when channelType == ChannelType.Register.
    val interval: String? = null,
    val direction: Direction,
    val unitOfMeasurement: UnitOfMeasurement,
    val firstReadingTimestamp: String,
    val lastReadingTimestamp: String,
    val telemetryUpdatedTimestamp: String,
    val lastChangeFeedCheckpoint: String,
)

enum class ChannelType {
    Profiel,
    Register, // Used for maandmaximum, maandtotaal
}

enum class ChannelDataType {
    Decimal,
    Text,
}

enum class UnitOfMeasurement {
    kW,
    kWh,
    kVA,
    kVarh,
    kVAr,
    m3,
    Onbekend,
}

enum class Direction {
    Consumption,
    Production,
    Unknown,
}

enum class ProductType {
    Electricity,
    Gas,
}
