package com.zenmo.zummon.companysurvey

fun interface Validator {
    fun validate(survey: Survey): List<ValidationResult>
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
    fun validate(survey: Survey): List<ValidationResult> {
        val results = mutableListOf<ValidationResult>()

        survey.addresses.forEach { address ->
            results.addAll(AddressValidator().validate(address))
        }

        return results
    }
}

class AddressValidator {
    fun validate(address: Address): List<ValidationResult> {
        val results = mutableListOf<ValidationResult>()

        address.gridConnections.forEach { gridConnection ->
            val gridConnectionResults = GridConnectionValidator().validate(gridConnection)
            results.addAll(gridConnectionResults)
        }

        return results
    }
}

class GridConnectionValidator {
    fun validate(gridConnection: GridConnection): List<ValidationResult> {
        val results = mutableListOf<ValidationResult>()

        results.addAll(ElectricityValidator().validate(gridConnection.electricity))
        results.addAll(TransportValidator().validate(gridConnection.transport))
        results.addAll(validateTotalPowerChargePoints(gridConnection))

        return results
    }
    // Validator for total charge point power < contracted capacity + battery power
    fun validateTotalPowerChargePoints(gridConnection: GridConnection): List<ValidationResult> {
        val results = mutableListOf<ValidationResult>()

        val totalPowerChargePoints = (gridConnection.transport.cars.powerPerChargePointKw ?: 0).toFloat() +
                (gridConnection.transport.trucks.powerPerChargePointKw ?: 0).toFloat() +
                (gridConnection.transport.vans.powerPerChargePointKw ?: 0).toFloat()

        val contractedCapacity = (gridConnection.electricity.getContractedConnectionCapacityKw() ?: 0.0).toFloat()
        val batteryPower = (gridConnection.storage.batteryPowerKw ?: 0.0).toFloat()

        when {
            totalPowerChargePoints < (contractedCapacity + batteryPower) -> results.add(ValidationResult(Status.VALID, "Total power of charge points is valid"))
            else -> results.add(ValidationResult(Status.INVALID, "Total power of charge points ${totalPowerChargePoints} exceeds allowed capacity ${contractedCapacity + batteryPower}"))
        }
        return results
    }
}

class ElectricityValidator {
    fun validate(electricity: Electricity): List<ValidationResult> {
        val results = mutableListOf<ValidationResult>()

        results.addAll(validateKleinOrGroot(electricity))
        results.add(validateContractedCapacity(electricity))
        results.add(validatePvProductionFeedIn(electricity))
        results.add(validateContractedFeedInCapacity(electricity))

        return results
    }

    fun validateKleinOrGroot(electricity: Electricity): List<ValidationResult> {
        val results = mutableListOf<ValidationResult>()
        val kleinOrGroot = electricity.kleinverbruikOrGrootverbruik

        if (kleinOrGroot == null) {
            results.add(ValidationResult(Status.MISSING_DATA, "Small or large consumption type is not defined"))
        } else {
            // Validate grootverbruik data
            if (kleinOrGroot == KleinverbruikOrGrootverbruik.GROOTVERBRUIK) {
                results.addAll(validateGrootverbruik(electricity.grootverbruik))
                results.add(validateGrootverbruikPhysicalCapacity(electricity))
            }

            // Validate kleinverbruik data
            if (kleinOrGroot == KleinverbruikOrGrootverbruik.KLEINVERBRUIK) {
                results.add(validateKleinverbruik(electricity))
            }
        }

        return results
    }

    fun validateGrootverbruik(grootverbruik: CompanyGrootverbruik?): List<ValidationResult> {
        val results = mutableListOf<ValidationResult>()

        if (grootverbruik == null) {
            results.add(ValidationResult(Status.INVALID, "Large consumption data is not provided"))
        } else {
            grootverbruik.contractedConnectionDeliveryCapacity_kW ?: results.add(ValidationResult(Status.MISSING_DATA, "Connection delivery capacity is not provided for large consumption"))
            grootverbruik.physicalCapacityKw ?: results.add(ValidationResult(Status.MISSING_DATA, "Physical Capacity Kw is not provided for large consumption"))
            grootverbruik.contractedConnectionFeedInCapacity_kW ?: results.add(ValidationResult(Status.MISSING_DATA, "Connection delivery capacity is not provided for large consumption"))
        }

        return results
    }

    // Validator for grootverbruik physical connection > 3x80A (55.2 kW)
    fun validateGrootverbruikPhysicalCapacity(electricity: Electricity): ValidationResult {
        val connectionCapacity = electricity.getPhysicalConnectionCapacityKw()

        return if (connectionCapacity == null) {
            ValidationResult(Status.MISSING_DATA, "No physical capacity data for Large Consumption")
        } else if (connectionCapacity > KleinverbruikElectricityConnectionCapacity._3x80A.toKw()) {
            ValidationResult(Status.VALID, "Large Consumption physical capacity is valid")
        } else {
            ValidationResult(Status.INVALID, "Large Consumption physical capacity ${connectionCapacity} is below 3x80A")
        }
    }

    fun validateKleinverbruik(electricity: Electricity): ValidationResult {
        return if (electricity.kleinverbruikOrGrootverbruik == KleinverbruikOrGrootverbruik.KLEINVERBRUIK) {
             if (kleinverbruik == null) {
                ValidationResult(Status.INVALID, "Small consumption data is not provided")
            } else if (kleinverbruik.connectionCapacity != null) {
                // Validate that kleinverbruik connection capacity <= 3x80A
                if (kleinverbruik.connectionCapacity > KleinverbruikElectricityConnectionCapacity._3x80A) {
                    ValidationResult(
                        Status.INVALID,
                        "Small consumption connection capacity  ${kleinverbruik.connectionCapacity} exceeds limit (3x80A)"
                    )
                } else {
                    ValidationResult(Status.VALID, "Small consumption connection capacity is within limits")
                }
            } else {
                ValidationResult(
                    Status.INVALID,
                    "Small consumption connection capacity ${kleinverbruik.connectionCapacity} is invalid"
                )
            }
        } else {
            ValidationResult(Status.NOT_APPLICABLE, "Small consumption validations are not applicable")
        }
    }

    // Validator for contracted delivery capacity <= physical capacity
    fun validateContractedCapacity(electricity: Electricity): ValidationResult {
        val contractedCapacity = electricity.getContractedConnectionCapacityKw()
        val physicalCapacity = electricity.getPhysicalConnectionCapacityKw()

        return when {
            contractedCapacity == null -> ValidationResult(Status.MISSING_DATA, "Connection delivery capacity is not provided")
            physicalCapacity == null -> ValidationResult(Status.MISSING_DATA, "Physical capacity is not provided")
            contractedCapacity <= physicalCapacity -> ValidationResult(Status.VALID, "Contracted delivery capacity is valid ${contractedCapacity}")
            else -> ValidationResult(Status.INVALID, "Contracted delivery capacity ${contractedCapacity} kW exceeds physical capacity ${physicalCapacity} kW")
        }
    }

    // Validator for contracted feed-in capacity <= physical capacity
    fun validateContractedFeedInCapacity(electricity: Electricity): ValidationResult {
        val feedInCapacity = electricity.getContractedFeedInCapacityKw()
        val physicalCapacity = electricity.getPhysicalConnectionCapacityKw()

        return when {
            feedInCapacity == null -> ValidationResult(Status.MISSING_DATA, "No feed-in capacity data")
            physicalCapacity == null -> ValidationResult(Status.MISSING_DATA, "No physical capacity data")
            feedInCapacity <= physicalCapacity -> ValidationResult(Status.VALID, "Feed-in capacity is valid")
            else -> ValidationResult(Status.INVALID, "Feed-in capacity ${feedInCapacity} exceeds physical capacity ${physicalCapacity}")
        }
    }

    // Validator for PV production >= feed-in
    fun validatePvProductionFeedIn(electricity: Electricity): ValidationResult {
        val annualProduction = electricity.annualElectricityProduction_kWh
        val feedIn = electricity.annualElectricityFeedIn_kWh

        return when {
            annualProduction == null -> ValidationResult(Status.MISSING_DATA, "No PV production data")
            feedIn == null -> ValidationResult(Status.MISSING_DATA, "No feed-in data")
            annualProduction >= feedIn -> ValidationResult(Status.VALID, "PV production is valid")
            else -> ValidationResult(Status.INVALID, "Annual PV production ${annualProduction} is less than feed-in ${feedIn}")
        }
    }


}

class TransportValidator {
    fun validate(transport: Transport): List<ValidationResult> {
        val results = mutableListOf<ValidationResult>()

        results.add(validatePowerPerChargeCars(transport))
        results.add(validatePowerPerChargeTrucks(transport))
        results.add(validatePowerPerChargeVans(transport))

        results.add(validateTravelDistanceCar(transport))
        results.add(validateTravelDistanceTruck(transport))
        results.add(validateTravelDistanceVan(transport))

        results.add(validateTotalElectricCars(transport))
        results.add(validateTotalElectricTrucks(transport))
        results.add(validateTotalElectricVans(transport))

        return results
    }

    // Validator for power per charge point in range 3..150 kW
    fun validatePowerPerChargeCars(transport: Transport): ValidationResult {
        val powerPerChargePointCars = transport.cars.powePerChargePointKw

        return when {
            powerPerChargePointCars == null -> ValidationResult(Status.NOT_APPLICABLE, "Cars Power per charge point is not provided")
            powerPerChargePointCars in 3.0..150.0 -> ValidationResult(Status.VALID, "Cars Power per charge point is valid")
            else -> ValidationResult(Status.INVALID, "Cars power per charge point ${powerPerChargePointCars} is outside the valid range (3..150 kW)")
        }
    }

    fun validatePowerPerChargeTrucks(transport: Transport): ValidationResult {
        val powerPerChargePointTrucks = transport.trucks.powerPerChargePointKw

        return when {
            powerPerChargePointTrucks == null -> ValidationResult(Status.NOT_APPLICABLE, "Trucks Power per charge point is not provided")
            powerPerChargePointTrucks in 3.0..150.0 -> ValidationResult(Status.VALID, "Trucks Power per charge point is valid")
            else -> ValidationResult(Status.INVALID, "Trucks power per charge point ${powerPerChargePointTrucks} is outside the valid range (3..150 kW)")
        }
    }

    fun validatePowerPerChargeVans(transport: Transport): ValidationResult {
        val powerPerChargePointVans = transport.vans.powerPerChargePointKw

        return when {
            powerPerChargePointVans == null -> ValidationResult(Status.NOT_APPLICABLE, "Vans Power per charge point is not provided")
            powerPerChargePointVans in 3.0..150.0 -> ValidationResult(Status.VALID, "Vans Power per charge point is valid")
            else -> ValidationResult(Status.INVALID, "Vans power per charge point ${powerPerChargePointVans} is outside the valid range (3..150 kW)")
        }
    }

    // Validator for vehicle travel distance in range 5k..100k km
    fun validateTravelDistanceCar(transport: Transport): ValidationResult {
        val travelDistanceCars = transport.cars.annualTravelDistancePerCarKm

        return when {
            travelDistanceCars == null -> ValidationResult(Status.MISSING_DATA, "Cars travel distance is not provided")
            travelDistanceCars in 5000..100000 -> ValidationResult(Status.VALID, "Cars travel distances are valid")
            else -> ValidationResult(Status.INVALID, "Cars travel distance ${travelDistanceCars} are outside the valid range (5k..100k km)")
        }
    }

    fun validateTravelDistanceTruck(transport: Transport): ValidationResult {
        val travelDistanceTrucks = transport.trucks.annualTravelDistancePerTruckKm

        return when {
            travelDistanceTrucks == null -> ValidationResult(Status.MISSING_DATA, "Trucks travel distance is not provided")
            travelDistanceTrucks in 5000..100000 -> ValidationResult(Status.VALID, "Trucks travel distances are valid")
            else -> ValidationResult(Status.INVALID, "Trucks travel distance ${travelDistanceTrucks} are outside the valid range (5k..100k km)")
        }
    }

    fun validateTravelDistanceVan(transport: Transport): ValidationResult {
        val travelDistanceVans = transport.vans.annualTravelDistancePerVanKm

        return when {
            travelDistanceVans == null -> ValidationResult(Status.MISSING_DATA, "Vans travel distance is not provided")
            travelDistanceVans in 5000..100000 -> ValidationResult(Status.VALID, "Vans travel distances are valid")
            else -> ValidationResult(Status.INVALID, "Vans travel distances ${travelDistanceVans} are outside the valid range (5k..100k km)")
        }
    }

    // Validator for number of electric vehicles should be less than or equal to total number of vehicles
    fun validateTotalElectricCars(transport: Transport): ValidationResult {
        return when {
            (transport.cars.numElectricCars ?: 0) > (transport.cars.numCars ?: 0) -> ValidationResult(Status.INVALID, "Number of electric cars ${transport.cars.numElectricCars} exceeds the total number of cars ${transport.cars.numCars}")
            else -> ValidationResult(Status.VALID, "Number of Electric Cars is lower than the total of Cars")
        }
    }

    fun validateTotalElectricTrucks(transport: Transport): ValidationResult {
        return when {
            (transport.trucks.numElectricTrucks ?: 0) > (transport.trucks.numTrucks ?: 0) -> ValidationResult(Status.INVALID, "Number of electric trucks ${transport.trucks.numTrucks} exceeds the total number of trucks ${transport.trucks.numTrucks}")
            else -> ValidationResult(Status.VALID, "Number of Electric Trucks is lower than the total of trucks")
        }
    }

    fun validateTotalElectricVans(transport: Transport): ValidationResult {
        return when {
            ((transport.vans.numElectricVans ?: 0) > (transport.vans.numVans ?: 0)) -> ValidationResult(Status.INVALID, "Number of electric vans ${transport.vans.numElectricVans} exceeds the total number of vans ${transport.vans.numVans}")
            else -> ValidationResult(Status.VALID, "Number of Electric Vans is lower than the total of Vans")
        }
    }
}
