package com.zenmo.zummon.companysurvey

fun interface Validator {
    fun validate(survey: Survey): ValidationResult
}

data class ValidationResult(
    val status: Status,
    val message: String,
)

enum class Status {
    VALID,
    INVALID,
    MISSING_DATA,
}

// Validator for contracted delivery capacity <= physical capacity
val validateContractedCapacity = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    val contractedCapacity = gridConnection.electricity.getContractedConnectionCapacityKw()
    val physicalCapacity = gridConnection.electricity.grootverbruik?.physicalCapacityKw

    when {
        contractedCapacity == null -> ValidationResult(Status.MISSING_DATA, "No contracted delivery capacity given")
        physicalCapacity == null -> ValidationResult(Status.MISSING_DATA, "No physical capacity given")
        contractedCapacity <= physicalCapacity -> ValidationResult(Status.VALID, "Contracted delivery capacity is valid")
        else -> ValidationResult(Status.INVALID, "Contracted delivery capacity exceeds physical capacity")
    }
}

// Validator for contracted feed-in capacity <= physical capacity
val validateContractedFeedInCapacity = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    val feedInCapacity = gridConnection.electricity.grootverbruik?.contractedConnectionFeedInCapacity_kW
    val physicalCapacity = gridConnection.electricity.grootverbruik?.physicalCapacityKw

    when {
        feedInCapacity == null -> ValidationResult(Status.MISSING_DATA, "No feed-in capacity given")
        physicalCapacity == null -> ValidationResult(Status.MISSING_DATA, "No physical capacity given")
        feedInCapacity <= physicalCapacity -> ValidationResult(Status.VALID, "Feed-in capacity is valid")
        else -> ValidationResult(Status.INVALID, "Feed-in capacity exceeds physical capacity")
    }
}

// Validator for PV production >= feed-in
val validatePvProduction = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    val pvProduction = gridConnection.electricity.annualElectricityProduction_kWh
    val feedIn = gridConnection.electricity.annualElectricityFeedIn_kWh

    when {
        pvProduction == null -> ValidationResult(Status.MISSING_DATA, "No PV production data")
        feedIn == null -> ValidationResult(Status.MISSING_DATA, "No feed-in data")
        pvProduction >= feedIn -> ValidationResult(Status.VALID, "PV production is valid")
        else -> ValidationResult(Status.INVALID, "PV production is less than feed-in")
    }
}

// Validator for grootverbruik physical connection > 3x80A (55.2 kW)
val validateGrootverbruikPhysicalCapacity = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    val physicalCapacity = gridConnection.electricity.grootverbruik?.physicalCapacityKw

    when {
        physicalCapacity == null -> ValidationResult(Status.MISSING_DATA, "No physical capacity for grootverbruik")
        physicalCapacity >= KleinverbruikElectricityConnectionCapacity._3x80A.toKw() -> ValidationResult(Status.VALID, "Grootverbruik physical capacity is valid")
        else -> ValidationResult(Status.INVALID, "Grootverbruik physical capacity is below 3x80A")
    }
}

// Validator for kleinverbruik physical connection <= 3x80A (55.2 kW)
val validateKleinverbruikPhysicalCapacity = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    val connectionCapacity = gridConnection.electricity.kleinverbruik?.connectionCapacity

    when {
        connectionCapacity == null -> ValidationResult(Status.MISSING_DATA, "No connection capacity for kleinverbruik")
        connectionCapacity <= KleinverbruikElectricityConnectionCapacity._3x80A -> ValidationResult(Status.VALID, "Kleinverbruik physical capacity is valid")
        else -> ValidationResult(Status.INVALID, "Kleinverbruik physical capacity exceeds 3x80A")
    }
}

// Validator for power per charge point in range 3..150 kW
val validatePowerPerChargePoint = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    val powerPerChargePointCars = (gridConnection.transport.cars.powerPerChargePointKw ?: 0).toFloat()
    val powerPerChargePointTrucks = (gridConnection.transport.trucks.powerPerChargePointKw ?: 0).toFloat()
    val powerPerChargePointVans = (gridConnection.transport.vans.powerPerChargePointKw ?: 0).toFloat()

    when {
        powerPerChargePointCars in 3.0..150.0 -> ValidationResult(Status.VALID, "Cars Power per charge point is valid")
        powerPerChargePointTrucks in 3.0..150.0 -> ValidationResult(Status.VALID, "Trucks Power per charge point is valid")
        powerPerChargePointVans in 3.0..150.0 -> ValidationResult(Status.VALID, "Vans Power per charge point is valid")
        else -> ValidationResult(Status.INVALID, "Power per charge point is outside the valid range (3..150 kW)")
    }
}

// Validator for total charge point power < contracted capacity + battery power
val validateTotalPowerChargePoints = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()

    val totalPowerChargePoints = (gridConnection.transport.cars.powerPerChargePointKw ?: 0).toFloat() +
            (gridConnection.transport.trucks.powerPerChargePointKw ?: 0).toFloat() +
            (gridConnection.transport.vans.powerPerChargePointKw ?: 0).toFloat()

    val contractedCapacity = (gridConnection.electricity.getContractedConnectionCapacityKw() ?: 0.0).toFloat()
    val batteryPower = (gridConnection.storage.batteryPowerKw ?: 0.0).toFloat()

    when {
        totalPowerChargePoints < (contractedCapacity + batteryPower) -> ValidationResult(Status.VALID, "Total power of charge points is valid")
        else -> ValidationResult(Status.INVALID, "Total power of charge points exceeds allowed capacity")
    }
}

// Validator for vehicle travel distance in range 5k..100k km
val validateVehicleTravelDistance = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    val travelDistanceCars = gridConnection.transport.cars.annualTravelDistancePerCarKm
    val travelDistanceTrucks = gridConnection.transport.trucks.annualTravelDistancePerTruckKm
    val travelDistanceVans = gridConnection.transport.vans.annualTravelDistancePerVanKm

    when {
        travelDistanceCars in 5000..100000 -> ValidationResult(Status.VALID, "Car Vehicle travel distances are valid")
        travelDistanceTrucks in 5000..100000 -> ValidationResult(Status.VALID, "Truck Vehicle travel distances are valid")
        travelDistanceVans in 5000..100000 -> ValidationResult(Status.VALID, "Van Vehicle travel distances are valid")
        else -> ValidationResult(Status.INVALID, "Vehicle travel distances are outside the valid range (5k..100k km)")
    }
}

// Validator for number of electric vehicles should be less than or equal to total number of vehicles
val validateTotalElectricVehicle = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()

    when {
        (gridConnection.transport.cars.numElectricCars ?: 0) > (gridConnection.transport.cars.numCars ?: 0) -> ValidationResult(Status.INVALID, "Electric Car Vehicle exceeds Number of Cars")
        (gridConnection.transport.trucks.numElectricTrucks ?: 0) > (gridConnection.transport.trucks.numTrucks ?: 0) -> ValidationResult(Status.INVALID, "Electric Car Vehicle exceeds Number of Cars")
        (gridConnection.transport.vans.numElectricVans ?: 0) > (gridConnection.transport.vans.numVans ?: 0) -> ValidationResult(Status.INVALID, "Electric Car Vehicle exceeds Number of Cars")
        else -> ValidationResult(Status.VALID, "Number of Electric Vehicles are valid")
    }
}

