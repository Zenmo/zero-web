package com.zenmo.companysurvey

import com.zenmo.companysurvey.dto.*
import com.zenmo.createSchema
import com.zenmo.plugins.connectToPostgres
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.BeforeClass
import java.util.UUID
import kotlin.test.Test

class RepositoryTest {
    companion object {
        /**
         * Drop and create database before running tests.
         */
        @JvmStatic
        @BeforeClass
        fun setupClass() {
            val db = connectToPostgres()
            val schema = Schema(db.connector().schema)
            transaction(db) {
                SchemaUtils.dropSchema(schema, cascade = true)
                SchemaUtils.createSchema(schema)
            }

            createSchema(db)
        }
    }

    @Test
    fun testSaveMinimalSurvey() {
        val db = connectToPostgres()
        val repo = SurveyRepository(db)
        val survey = Survey(
            companyName = "Zenmo",
            zenmoProject = "Project",
            personName = "John Doe",
            email = "john@example.com",
            addresses = emptyList(),
        )
        repo.save(survey)
    }

    @Test
    fun testSaveWithGridConnections() {
        val db = connectToPostgres()
        val repo = SurveyRepository(db)

        val addressId = UUID.randomUUID()

        val survey = Survey(
            companyName = "Zenmo",
            zenmoProject = "Project",
            personName = "John Doe",
            email = "john@example.com",
            addresses = listOf(
                Address(
                    id = addressId,
                    street = "Kerkstraat",
                    houseNumber = 1,
                    houseLetter = "A",
                    houseNumberSuffix = "100",
                    postalCode = "1234AB",
                    city = "Amsterdam",
                    gridConnections = listOf(
                        GridConnection(
                            electricity = Electricity(
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
                                numDailyCarCommuters = null,
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
        repo.save(survey)
    }
}