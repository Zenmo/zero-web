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
            houseNumber = 1,
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
                        quarterHourlyValuesFiles = emptyList(),
                        grootverbruik = CompanyGrootverbruik(
                            contractedConnectionDemandCapacityKw = 100,
                            contractedConnectionSupplyCapacityKw = 200,
                        ),
                        kleinverbruik = CompanyKleinverbruik(
                            connectionCapacity = KleinverbruikElectricityConnectionCapacity.`3x63A`,
                            consumptionProfile = KleinverbruikElectricityConsumptionProfile.TWO,
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
                        otherSupply = "Other supply",
                        missingPvReason = MissingPvReason.OTHER,
                    ),
                    naturalGas = NaturalGas(
                        ean = "123456789012345678",
                        hasConnection = true,
                        annualDemandM3 = 3500,
                        hourlyValuesFiles = emptyList(),
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
                        numDailyCarAndVanCommuters = null,
                        numDailyCarVisitors = null,
                        numCommuterAndVisitorChargePoints = 2u,
                        cars = Cars(
                            numCars = null,
                            numElectricCars = 0,
                            numChargePoints = 0,
                            powerPerChargePointKw = 0f,
                            annualTravelDistancePerCarKm = 0,
                            numPlannedElectricCars = 0,
                        ),
                        trucks = Trucks(
                            numTrucks = null,
                            numElectricTrucks = 0,
                            numChargePoints = 0,
                            powerPerChargePointKw = 0f,
                            annualTravelDistancePerTruckKm = 0,
                            numPlannedElectricTrucks = 0,
                        ),
                        vans = Vans(
                            numVans = null,
                            numElectricVans = 0,
                            numChargePoints = 0,
                            powerPerChargePointKw = 0f,
                            annualTravelDistancePerVanKm = 0,
                            numPlannedElectricVans = 0,
                        ),
                    ),
                )
            ),
        )
    )
)