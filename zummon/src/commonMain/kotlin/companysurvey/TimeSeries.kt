package com.zenmo.zummon.companysurvey

import kotlinx.serialization.Serializable

/**
 * This contains values parsed from a CSV/excel or fetched from an API.
 */
@Serializable
data class TimeSeries (
    // Should be every 15 minutes for electricity
    // and every hour for gas.
    val dataPoints: List<TimeSeriesDataPoint> = emptyList(),
) {
    fun getFlatDataPoints(): FloatArray =
        dataPoints.foldIndexed(FloatArray(dataPoints.size)) { i, array, dataPoint ->
            array[i] = dataPoint.value; array
        }
}
