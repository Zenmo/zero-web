package com.zenmo.orm.companysurvey.dto

import kotlinx.serialization.Serializable

/**
 * This contains values parsed from the CSV/excel or fetched from an API.
 * Not implemented yet.
 */
@Serializable
data class QuarterHourlyElectricityUsage (
    // should be every 15 minutes
    val timestamp: kotlinx.datetime.Instant,
    val kwh: Float,
)
