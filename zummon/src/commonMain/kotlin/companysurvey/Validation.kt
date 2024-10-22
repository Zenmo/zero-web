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
    NOT_APPLICABLE,
}


class SurveyValidator {
    // List of validators
    private val validators: List<Validator> = listOf(
        validateContractedCapacity,
        validateContractedFeedInCapacity,
        validatePvProduction,
        validateGrootverbruikPhysicalCapacity,
        validateKleinverbruikPhysicalCapacity,
        validatePowerPerChargeCars,
        validatePowerPerChargeTrucks,
        validatePowerPerChargeVans,
        validateTotalPowerChargePoints,
        validateCarTravelDistance,
        validateTruckTravelDistance,
        validateVanTravelDistance,
        validateTotalElectricCars,
        validateTotalElectricTrucks,
        validateTotalElectricVans
    )

    // Function to run all validators and collect results
    fun validateSurvey(survey: Survey): List<ValidationResult> {
        return validators.map { it.validate(survey) }
    }
}

// Validator if Grootverbruik includes Grootverbruik data
val validateKleinverbruikOrGrootverbruik = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()

    when (gridConnection.electricity.kleinverbruikOrGrootverbruik) {
        KleinverbruikOrGrootverbruik.GROOTVERBRUIK -> {
            gridConnection.electricity.grootverbruik?.let {
                ValidationResult(Status.VALID, "Large consumption data has been provided")
            } ?: ValidationResult(Status.INVALID, "Large consumption data is not provided")
        }
        KleinverbruikOrGrootverbruik.KLEINVERBRUIK -> {
            gridConnection.electricity.kleinverbruik?.let {
                ValidationResult(Status.VALID, "Small consumption data has been provided")
            } ?: ValidationResult(Status.INVALID, "Small consumption data is not provided")
        }
        else -> ValidationResult(Status.INVALID, "Neither small nor large consumption type is provided")
    }
}

// Validator GROOTVERBRUIK has required values
val validateGrootverbruikCapacities = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    if (gridConnection.electricity.kleinverbruikOrGrootverbruik == KleinverbruikOrGrootverbruik.GROOTVERBRUIK) {
        val grootverbruik = gridConnection.electricity.grootverbruik
        grootverbruik?.contractedConnectionDeliveryCapacity_kW ?: ValidationResult(Status.MISSING_DATA, "Connection delivery capacity is not provided for large consumption")
        grootverbruik?.physicalCapacityKw ?: ValidationResult(Status.MISSING_DATA, "Physical CapacityKw is not provided for large consumption")
        grootverbruik?.contractedConnectionFeedInCapacity_kW ?: ValidationResult(Status.MISSING_DATA, "Connection delivery capacity is not provided for large consumption")
    }
    ValidationResult(Status.NOT_APPLICABLE, "large consumption values has been reviewed")
}

// Validator for contracted delivery capacity <= physical capacity
val validateContractedCapacity = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    val contractedCapacity = gridConnection.electricity.getContractedConnectionCapacityKw()
    val physicalCapacity = gridConnection.electricity.getPhysicalConnectionCapacityKw()

    when {
        contractedCapacity == null -> ValidationResult(Status.MISSING_DATA, "Connection delivery capacity is not provided")
        physicalCapacity == null -> ValidationResult(Status.MISSING_DATA, "Physical capacity is not provided")
        contractedCapacity <= physicalCapacity -> ValidationResult(Status.VALID, "Contracted delivery capacity is valid")
        else -> ValidationResult(Status.INVALID, "Contracted delivery capacity exceeds physical capacity")
    }
}

// Validator for contracted feed-in capacity <= physical capacity
val validateContractedFeedInCapacity = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    val feedInCapacity = gridConnection.electricity.getContractedFeedInCapacityKw()
    val physicalCapacity = gridConnection.electricity.getPhysicalConnectionCapacityKw()

    when {
        feedInCapacity == null -> ValidationResult(Status.MISSING_DATA, "No feed-in capacity data")
        physicalCapacity == null -> ValidationResult(Status.MISSING_DATA, "No physical capacity data")
        feedInCapacity <= physicalCapacity -> ValidationResult(Status.VALID, "Feed-in capacity is valid")
        else -> ValidationResult(Status.INVALID, "Feed-in capacity exceeds physical capacity")
    }
}

// Validator for PV production >= feed-in
val validatePvProduction = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    val annualProduction = gridConnection.electricity.annualElectricityProduction_kWh
    val feedIn = gridConnection.electricity.annualElectricityFeedIn_kWh

    when {
        annualProduction == null -> ValidationResult(Status.MISSING_DATA, "No PV production data")
        feedIn == null -> ValidationResult(Status.MISSING_DATA, "No feed-in data")
        annualProduction >= feedIn -> ValidationResult(Status.VALID, "PV production is valid")
        else -> ValidationResult(Status.INVALID, "Annual PV production is less than feed-in")
    }
}

// Validator for grootverbruik physical connection > 3x80A (55.2 kW)
val validateGrootverbruikPhysicalCapacity = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    val connectionCapacity = gridConnection.electricity.getPhysicalConnectionCapacityKw()

    when (gridConnection.electricity.kleinverbruikOrGrootverbruik) {
        KleinverbruikOrGrootverbruik.GROOTVERBRUIK -> {
            if (connectionCapacity == null) {
                ValidationResult(Status.MISSING_DATA, "No physical capacity data for large consumption")
            } else if (connectionCapacity >= KleinverbruikElectricityConnectionCapacity._3x80A.toKw()) {
                ValidationResult(Status.VALID, "Large consumption physical capacity is valid")
            } else {
                ValidationResult(Status.INVALID, "Large consumption physical capacity is below 3x80A")
            }
        }
        else -> ValidationResult(Status.NOT_APPLICABLE, "Large consumption validation)")
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
val validatePowerPerChargeCars = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    val powerPerChargePointCars = gridConnection.transport.cars.powerPerChargePointKw

    when {
        powerPerChargePointCars == null -> ValidationResult(Status.NOT_APPLICABLE, "Cars Power per charge point is not provided")
        powerPerChargePointCars in 3.0..150.0 -> ValidationResult(Status.VALID, "Cars Power per charge point is valid")
        else -> ValidationResult(Status.INVALID, "Cars power per charge point is outside the valid range (3..150 kW)")
    }
}

val validatePowerPerChargeTrucks = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    val powerPerChargePointTrucks = gridConnection.transport.trucks.powerPerChargePointKw

    when {
        powerPerChargePointTrucks == null -> ValidationResult(Status.NOT_APPLICABLE, "Trucks Power per charge point is not provided")
        powerPerChargePointTrucks in 3.0..150.0 -> ValidationResult(Status.VALID, "Trucks Power per charge point is valid")
        else -> ValidationResult(Status.INVALID, "Trucks power per charge point is outside the valid range (3..150 kW)")
    }
}

val validatePowerPerChargeVans = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    val powerPerChargePointVans = gridConnection.transport.vans.powerPerChargePointKw

    when {
        powerPerChargePointVans == null -> ValidationResult(Status.NOT_APPLICABLE, "Vans Power per charge point is not provided")
        powerPerChargePointVans in 3.0..150.0 -> ValidationResult(Status.VALID, "Vans Power per charge point is valid")
        else -> ValidationResult(Status.INVALID, "Vans power per charge point is outside the valid range (3..150 kW)")
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
val validateCarTravelDistance = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    val travelDistanceCars = gridConnection.transport.cars.annualTravelDistancePerCarKm

    when {
        travelDistanceCars == null -> ValidationResult(Status.MISSING_DATA, "Cars travel distance is not provided")
        travelDistanceCars in 5000..100000 -> ValidationResult(Status.VALID, "Cars travel distances are valid")
        else -> ValidationResult(Status.INVALID, "Cars travel distance are outside the valid range (5k..100k km)")
    }
}
val validateTruckTravelDistance = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    val travelDistanceTrucks = gridConnection.transport.trucks.annualTravelDistancePerTruckKm

    when {
        travelDistanceTrucks == null -> ValidationResult(Status.MISSING_DATA, "Trucks travel distance is not provided")
        travelDistanceTrucks in 5000..100000 -> ValidationResult(Status.VALID, "Trucks travel distances are valid")
        else -> ValidationResult(Status.INVALID, "Trucks travel distances are outside the valid range (5k..100k km)")
    }
}
val validateVanTravelDistance = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    val travelDistanceVans = gridConnection.transport.vans.annualTravelDistancePerVanKm

    when {
        travelDistanceVans == null -> ValidationResult(Status.MISSING_DATA, "Vans travel distance is not provided")
        travelDistanceVans in 5000..100000 -> ValidationResult(Status.VALID, "Vans travel distances are valid")
        else -> ValidationResult(Status.INVALID, "Vans travel distances are outside the valid range (5k..100k km)")
    }
}

// Validator for number of electric vehicles should be less than or equal to total number of vehicles
val validateTotalElectricCars = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    when {
        (gridConnection.transport.cars.numElectricCars ?: 0) > (gridConnection.transport.cars.numCars ?: 0) -> ValidationResult(Status.INVALID, "Number of electric cars exceeds the total number of cars")
        else -> ValidationResult(Status.VALID, "Number of Electric Cars are valid")
    }
}

val validateTotalElectricTrucks = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    when {
        (gridConnection.transport.trucks.numElectricTrucks ?: 0) > (gridConnection.transport.trucks.numTrucks ?: 0) -> ValidationResult(Status.INVALID, "Number of electric trucks exceeds the total number of trucks")
        else -> ValidationResult(Status.VALID, "Number of Electric Trucks are valid")
    }
}

val validateTotalElectricVans = Validator { survey: Survey ->
    val gridConnection = survey.getSingleGridConnection()
    when {
        (gridConnection.transport.vans.numElectricVans ?: 0) > (gridConnection.transport.vans.numVans ?: 0) -> ValidationResult(Status.INVALID, "Number of electric vans exceeds the total number of vans")
        else -> ValidationResult(Status.VALID, "Number of Electric Vans are valid")
    }
}

