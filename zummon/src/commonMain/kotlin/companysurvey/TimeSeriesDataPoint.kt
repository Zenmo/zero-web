package com.zenmo.zummon.companysurvey

import kotlinx.serialization.Serializable

@Serializable
data class TimeSeriesDataPoint (
    val timestamp: kotlinx.datetime.Instant,
    val value: Float,
)