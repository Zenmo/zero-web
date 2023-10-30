package com.zenmo.companysurvey.dto

import kotlinx.serialization.Serializable

@Serializable
class Storage(
    val hasBattery: Boolean?,
    val batteryCapacityKwh: Float?,
    val batteryPowerKw: Float?,
    val batterySchedule: String,

    val hasPlannedBattery: Boolean?,
    val plannedBatteryCapacityKwh: Float?,
    val plannedBatteryPowerKw: Float?,
    val plannedBatterySchedule: String,
)