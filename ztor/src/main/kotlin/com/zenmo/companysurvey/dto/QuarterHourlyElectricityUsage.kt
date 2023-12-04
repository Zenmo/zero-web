package com.zenmo.companysurvey.dto

import kotlinx.serialization.Serializable

/**
 * This contains values parsed from the CSV/excel.
 * Not implemented yet.
 */
@Serializable
class QuarterHourlyElectricityUsage (
    // should be every 15 minutes
    val timestamp: kotlinx.datetime.Instant,
    val kwh: Float,
)
