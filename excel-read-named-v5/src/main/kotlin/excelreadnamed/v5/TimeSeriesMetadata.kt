package com.zenmo.excelreadnamed.v5

import com.zenmo.zummon.companysurvey.TimeSeriesType
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
    levering {
        override fun timeSeriesType() = TimeSeriesType.ELECTRICITY_DELIVERY
    },
    teruglevering {
        override fun timeSeriesType() = TimeSeriesType.ELECTRICITY_FEED_IN
    },
    zon {
        override fun timeSeriesType() = TimeSeriesType.ELECTRICITY_PRODUCTION
    },
    gas {
        override fun timeSeriesType() = TimeSeriesType.GAS_DELIVERY
    };

    abstract fun timeSeriesType(): TimeSeriesType
}
