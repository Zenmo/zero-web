package com.zenmo.models.companysurvey

import kotlinx.serialization.Serializable

@Serializable
data class CompanyTransport (
    val hasVehicles: Boolean?,
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
    val powerPerChargePointKw: Int?,
    val annualTravelDistancePerTruckKm: Int?,

    // Disagreement on whether we should ask specifics about charge points
    val numPlannedElectricTrucks: Int?,
)

@Serializable
data class Vans (
    val numVans: Int?,
    val numElectricVans: Int?,
    val numChargePoints: Int?,
    val powerPerChargePointKw: Int?,
    val annualTravelDistancePerVanKm: Int?,

    val numPlannedElectricVans: Int?,
)

@Serializable
data class Cars (
    val numDailyCarCommutersAndVisitors: Int?,
    val numCars: Int?,
    val numElectricCars: Int?,
    val numChargePoints: Int?,
    val powerPerChargePointKw: Int?,
    val annualTravelDistancePerCarKm: Int?,

    val numPlannedElectricVans: Int?,
)
