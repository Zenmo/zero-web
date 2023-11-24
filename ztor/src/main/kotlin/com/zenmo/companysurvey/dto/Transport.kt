package com.zenmo.companysurvey.dto

import kotlinx.serialization.Serializable

@Serializable
data class Transport (
    val hasVehicles: Boolean?,
    val numDailyCarCommuters: Int?,
    val trucks: Trucks,
    val vans: Vans,
    val cars: Cars,
)

@Serializable
data class Trucks (
    // Current situation
    val numTrucks: Int?, // Total number including electric
    val numElectricTrucks: Int?,
    val numChargePoints: Int?,
    val powerPerChargePointKw: Float?,
    val annualTravelDistancePerTruckKm: Int?,

    // Disagreement on whether we should ask specifics about planned charge points
    val numPlannedElectricTrucks: Int?,
)

@Serializable
data class Vans (
    val numVans: Int?,
    val numElectricVans: Int?,
    val numChargePoints: Int?,
    val powerPerChargePointKw: Float?,
    val annualTravelDistancePerVanKm: Int?,

    val numPlannedElectricVans: Int?,
)

@Serializable
data class Cars (
    val numCars: Int?,
    val numElectricCars: Int?,
    val numChargePoints: Int?,
    val powerPerChargePointKw: Float?,
    val annualTravelDistancePerCarKm: Int?,

    val numPlannedElectricCars: Int?,
)
