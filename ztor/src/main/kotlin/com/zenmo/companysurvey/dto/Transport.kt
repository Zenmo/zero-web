package com.zenmo.companysurvey.dto

import kotlinx.serialization.Serializable

@Serializable
data class Transport (
    val hasVehicles: Boolean? = null,
    val numDailyCarCommuters: Int? = null,
    val trucks: Trucks = Trucks(),
    val vans: Vans = Vans(),
    val cars: Cars = Cars(),
)

@Serializable
data class Trucks (
    // Current situation
    val numTrucks: Int? = null, // Total number including electric
    val numElectricTrucks: Int? = null,
    val numChargePoints: Int? = null,
    val powerPerChargePointKw: Float? = null,
    val annualTravelDistancePerTruckKm: Int? = null,

    // Disagreement on whether we should ask specifics about planned charge points
    val numPlannedElectricTrucks: Int? = null,
)

@Serializable
data class Vans (
    val numVans: Int? = null,
    val numElectricVans: Int? = null,
    val numChargePoints: Int? = null,
    val powerPerChargePointKw: Float? = null,
    val annualTravelDistancePerVanKm: Int? = null,

    val numPlannedElectricVans: Int? = null,
)

@Serializable
data class Cars (
    val numCars: Int? = null,
    val numElectricCars: Int? = null,
    val numChargePoints: Int? = null,
    val powerPerChargePointKw: Float? = null,
    val annualTravelDistancePerCarKm: Int? = null,

    val numPlannedElectricCars: Int? = null,
)
