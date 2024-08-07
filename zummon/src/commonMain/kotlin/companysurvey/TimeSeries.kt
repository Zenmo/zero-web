package com.zenmo.zummon.companysurvey

import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.minutes
import com.benasher44.uuid.Uuid
import com.zenmo.zummon.UuidSerializer

/**
 * This contains values parsed from a CSV/excel or fetched from an API.
 */
@Serializable
data class TimeSeries (
    @Serializable(with = UuidSerializer::class)
    val id: Uuid? = null,
    val type: TimeSeriesType,
    // Measurement start time
    val start: kotlinx.datetime.Instant,
    val timeStep: kotlin.time.Duration = 15.minutes,
    val unit: TimeSeriesUnit = TimeSeriesUnit.KWH,
    val values: FloatArray = floatArrayOf(),
) {
    @Deprecated("Use .values", ReplaceWith("values"))
    fun getFlatDataPoints(): FloatArray = values
}

enum class TimeSeriesUnit {
    KWH,
    M3,
}

enum class TimeSeriesType {
    // Delivery from grid to end-user
    ELECTRICITY_DELIVERY,
    // Feed-in of end-user back in to the rid
    ELECTRICITY_FEED_IN,
    // Solar panel production
    ELECTRICITY_PRODUCTION,
    GAS_DELIVERY,
}
