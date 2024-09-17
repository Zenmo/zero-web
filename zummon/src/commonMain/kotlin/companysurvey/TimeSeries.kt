package com.zenmo.zummon.companysurvey

import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.minutes
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import com.zenmo.zummon.BenasherUuidSerializer
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


/**
 * This contains values parsed from a CSV/excel or fetched from an API.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class TimeSeries (
    @Serializable(with = BenasherUuidSerializer::class)
    val id: Uuid = uuid4(),
    val type: TimeSeriesType,
    // Measurement start time
    val start: kotlinx.datetime.Instant,
    val timeStep: kotlin.time.Duration = 15.minutes,
    val unit: TimeSeriesUnit = TimeSeriesUnit.KWH,
    val values: FloatArray = floatArrayOf(),
) {
    @Deprecated("Use .values", ReplaceWith("values"))
    fun getFlatDataPoints(): FloatArray = values

    /**
     * Generated code
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as TimeSeries

        if (id != other.id) return false
        if (type != other.type) return false
        if (start != other.start) return false
        if (timeStep != other.timeStep) return false
        if (unit != other.unit) return false
        if (!values.contentEquals(other.values)) return false

        return true
    }

    /**
     * Generated code
     */
    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + type.hashCode()
        result = 31 * result + start.hashCode()
        result = 31 * result + timeStep.hashCode()
        result = 31 * result + unit.hashCode()
        result = 31 * result + values.contentHashCode()
        return result
    }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
enum class TimeSeriesUnit {
    KWH,
    M3,
}

@OptIn(ExperimentalJsExport::class)
@JsExport
enum class TimeSeriesType {
    // Delivery from grid to end-user
    ELECTRICITY_DELIVERY,
    // Feed-in of end-user back in to the rid
    ELECTRICITY_FEED_IN,
    // Solar panel production
    ELECTRICITY_PRODUCTION,
    GAS_DELIVERY,
}
