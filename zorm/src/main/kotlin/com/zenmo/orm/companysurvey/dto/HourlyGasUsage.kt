package com.zenmo.orm.companysurvey.dto

import kotlinx.serialization.Serializable

/**
 * This contains data from the parsed CSV/excel
 * or data fetched through an API for which the Company gave us permission to access.
 */
@Serializable
data class HourlyGasUsage(
    val timestamp: kotlinx.datetime.Instant,
    val m3: Float,
)
