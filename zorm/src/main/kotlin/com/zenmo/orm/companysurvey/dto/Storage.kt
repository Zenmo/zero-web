package com.zenmo.orm.companysurvey.dto

import kotlinx.serialization.Serializable

@Serializable
data class Storage(
    val hasBattery: Boolean? = null,
    val batteryCapacityKwh: Float? = null,
    val batteryPowerKw: Float? = null,
    val batterySchedule: String = "",

    val hasPlannedBattery: Boolean? = null,
    val plannedBatteryCapacityKwh: Float? = null,
    val plannedBatteryPowerKw: Float? = null,
    val plannedBatterySchedule: String = "",

    val hasThermalStorage: Boolean? = null,
    val thermalStorageKw: Float? = null,
)
