package com.zenmo.companysurvey

import com.zenmo.companysurvey.dto.*
import java.util.*

val mockSurvey = Survey(
    companyName = "Zenmo",
    zenmoProject = "Project",
    personName = "John Doe",
    email = "john@example.com",
    dataSharingAgreed = true,
    addresses = listOf(
        Address(
            id = UUID.randomUUID(),
            street = "Kerkstraat",
            houseNumber = 1u,
            houseLetter = "A",
            houseNumberSuffix = "100",
            postalCode = "1234AB",
            city = "Amsterdam",
            gridConnections = listOf(
                GridConnection(
                    electricity = Electricity(
                        hasConnection = true,
                        annualElectricityDemandKwh = 1000,
                        annualElectricityProductionKwh = 2000,
                        ean = "123456789012345678",
                        quarterHourlyValuesFiles = listOf(
                            File(
                                blobName = "qwerty-kwartierwaarden-2021.csv",
                                originalName = "kwartierwaarden-2021.csv",
                                contentType = "text/csv",
                                size = 1000,
                            ),
                            File(
                                blobName = "qwerty-kwartierwaarden-2022.csv",
                                originalName = "kwartierwaarden-2022.csv",
                                contentType = "text/csv",
                                size = 1000,
                            ),
                        ),
                        grootverbruik = CompanyGrootverbruik(
                            contractedConnectionDemandCapacityKw = 100,
                            contractedConnectionSupplyCapacityKw = 200,
                            physicalCapacityKw = 300u,
                        ),
                        kleinverbruik = CompanyKleinverbruik(
                            connectionCapacity = KleinverbruikElectricityConnectionCapacity.`3x63A`,
                            consumptionProfile = KleinverbruikElectricityConsumptionProfile.TWO,
                        ),
                        authorizationFile = File(
                            blobName = "authorization.pdf",
                            originalName = "Authorization.pdf",
                            contentType = "application/pdf",
                            size = 10000,
                        ),
                        gridExpansion = GridExpansion(
                            hasRequestAtGridOperator = true,
                            requestedKW = 200u,
                            reason = "Mooaar power!",
                        ),
                    ),
                    supply = Supply(
                        hasSupply = true,
                        pvInstalledKwp = 100,
                        pvOrientation = PVOrientation.SOUTH,
                        pvPlanned = true,
                        pvPlannedKwp = 200,
                        pvPlannedOrientation = PVOrientation.EAST_WEST,
                        pvPlannedYear = 2022,
                        windInstalledKw = 300f,
                        windPlannedKw = 400f,
                        otherSupply = "Other supply",
                        missingPvReason = MissingPvReason.OTHER,
                    ),
                    naturalGas = NaturalGas(
                        ean = "123456789012345678",
                        hasConnection = true,
                        annualDemandM3 = 3500,
                        hourlyValuesFiles = listOf(
                            File(
                                blobName = "qwerty-uurwaarden-2022.csv",
                                originalName = "uurwaarden-2022.csv",
                                contentType = "text/csv",
                                size = 1000,
                            ),
                        ),
                        percentageUsedForHeating = 50,
                    ),
                    heat = Heat(
                        heatingTypes = listOf(HeatingType.GAS_BOILER, HeatingType.DISTRICT_HEATING),
                        sumGasBoilerKw = 28.8f,
                        sumHeatPumpKw = 0f,
                        sumHybridHeatPumpElectricKw = 0f,
                        annualDistrictHeatingDemandGj = 300f,
                        localHeatExchangeDescription = "Local heat exchange description",
                        hasUnusedResidualHeat = false,
                    ),
                    storage = Storage(
                        hasBattery = false,
                        batteryCapacityKwh = 0f,
                        batteryPowerKw = 0f,
                        batterySchedule = "Battery schedule",
                        hasPlannedBattery = true,
                        plannedBatteryCapacityKwh = 100f,
                        plannedBatteryPowerKw = 10f,
                        plannedBatterySchedule = "Planned battery schedule",
                        hasThermalStorage = true,
                    ),
                    mainConsumptionProcess = "Main consumption process",
                    electrificationPlans = "Electrification plans",
                    consumptionFlexibility = "Consumption flexibility",
                    energyOrBuildingManagementSystemSupplier = "EnergyBrothers",
                    surveyFeedback = "Survey feedback",
                    transport = Transport(
                        hasVehicles = false,
                        numDailyCarAndVanCommuters = 14,
                        numDailyCarVisitors = 5u,
                        numCommuterAndVisitorChargePoints = 2u,
                        cars = Cars(
                            numCars = 2,
                            numElectricCars = 0,
                            numChargePoints = 0,
                            powerPerChargePointKw = 0f,
                            annualTravelDistancePerCarKm = 0,
                            numPlannedElectricCars = 0,
                            numPlannedHydrogenCars = 2,
                        ),
                        trucks = Trucks(
                            numTrucks = 5,
                            numElectricTrucks = 0,
                            numChargePoints = 0,
                            powerPerChargePointKw = 0f,
                            annualTravelDistancePerTruckKm = 0,
                            numPlannedElectricTrucks = 0,
                            numPlannedHydrogenTrucks = 2,
                        ),
                        vans = Vans(
                            numVans = 2,
                            numElectricVans = 0,
                            numChargePoints = 0,
                            powerPerChargePointKw = 0f,
                            annualTravelDistancePerVanKm = 0,
                            numPlannedElectricVans = 0,
                            numPlannedHydrogenVans = 2,
                        ),
                        otherVehicles = OtherVehicles(
                            hasOtherVehicles = true,
                            electricRatio = .4f,
                        )
                    ),
                )
            ),
        )
    )
)