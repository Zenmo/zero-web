package com.zenmo.excelreadnamed.v3

import com.zenmo.zummon.companysurvey.TimeSeriesUnit

/**
 * The spreadsheet contains 6 timeseries.
 * This is the metadata for one timeseries.
 */
data class TimeSeriesMetadata(
    val index: Int, // 1 to 6
    val jaar: Int,
    val tijdzone: String,
    val resolutieMinuten: Int,
    val eenheid: TimeSeriesUnit,
    val soortProfiel: SoortProfiel,
    val profielCompleet: Boolean,
)

enum class SoortProfiel {
    levering,
    teruglevering,
    zon,
    gas,
}
