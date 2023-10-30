package com.zenmo.companysurvey.dto

/**
 * This contains values parsed from the CSV/excel.
 * Not implemented yet.
 */
class ElectricityUsage (
    // should be every 15 minutes
    val timestamp: kotlinx.datetime.Instant,
    val kwh: Double,
)
