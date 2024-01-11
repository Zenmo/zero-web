package com.zenmo.companysurvey

import com.zenmo.blob.BlobPurpose
import com.zenmo.companysurvey.dto.GridConnection
import com.zenmo.companysurvey.dto.Survey
import com.zenmo.companysurvey.table.AddressTable
import com.zenmo.companysurvey.table.GridConnectionTable
import com.zenmo.companysurvey.table.CompanySurveyTable
import com.zenmo.companysurvey.table.FileTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class SurveyRepository(
    private val db: Database
) {
    fun getHessenpoortSurveys(): List<Survey> {
        return listOf(mockSurvey, mockSurvey)
    }

    fun save(survey: Survey): UUID {
        val surveyId = UUID.randomUUID()

        transaction(db) {
            CompanySurveyTable.insert {
                it[id] = surveyId
                it[created] = survey.created
                it[project] = survey.zenmoProject
                it[companyName] = survey.companyName
                it[personName] = survey.personName
                it[email] = survey.email
            }

            AddressTable.batchInsert(survey.addresses) {
                address ->
                this[AddressTable.id] = address.id
                this[AddressTable.surveyId] = surveyId
                this[AddressTable.street] = address.street
                this[AddressTable.houseNumber] = address.houseNumber.toUInt()
                this[AddressTable.houseLetter] = address.houseLetter
                this[AddressTable.houseNumberSuffix] = address.houseNumberSuffix
                this[AddressTable.postalCode] = address.postalCode
                this[AddressTable.city] = address.city
            }

            GridConnectionTable.batchInsert(survey.addresses.flatMap { address ->
                address.gridConnections.map { gridConnection ->
                    Pair(
                        address.id,
                        gridConnection,
                    )
                }
            }) { pair: Pair<UUID, GridConnection> ->
                val (addressId, gridConnection) = pair

                this[GridConnectionTable.id] = gridConnection.id
                this[GridConnectionTable.addressId] = addressId

                // open questions
                this[GridConnectionTable.energyOrBuildingManagementSystemSupplier] = gridConnection.energyOrBuildingManagementSystemSupplier
                this[GridConnectionTable.mainConsumptionProcess] = gridConnection.mainConsumptionProcess
                this[GridConnectionTable.consumptionFlexibility] = gridConnection.consumptionFlexibility
                this[GridConnectionTable.electrificationPlans] = gridConnection.electrificationPlans
                this[GridConnectionTable.surveyFeedback] = gridConnection.surveyFeedback

                this[GridConnectionTable.hasVehicles] = gridConnection.transport.hasVehicles
                this[GridConnectionTable.numDailyCarCommuters] = gridConnection.transport.numDailyCarCommuters?.toUInt()
                this[GridConnectionTable.numCommuterChargePoints] = gridConnection.transport.numCommuterChargePoints

                this[GridConnectionTable.numTrucks] = gridConnection.transport.trucks.numTrucks?.toUInt()
                this[GridConnectionTable.numElectricTrucks] = gridConnection.transport.trucks.numElectricTrucks?.toUInt()
                this[GridConnectionTable.numTruckChargePoints] = gridConnection.transport.trucks.numChargePoints?.toUInt()
                this[GridConnectionTable.powerPerTruckChargePointKw] = gridConnection.transport.trucks.powerPerChargePointKw
                this[GridConnectionTable.annualTravelDistancePerTruckKm] = gridConnection.transport.trucks.annualTravelDistancePerTruckKm?.toUInt()
                this[GridConnectionTable.numPlannedElectricTrucks] = gridConnection.transport.trucks.numPlannedElectricTrucks?.toUInt()
                this[GridConnectionTable.numPlannedHydgrogenTrucks] = gridConnection.transport.trucks.numPlannedHydrogenTrucks?.toUInt()

                this[GridConnectionTable.numVans] = gridConnection.transport.vans.numVans?.toUInt()
                this[GridConnectionTable.numElectricVans] = gridConnection.transport.vans.numElectricVans?.toUInt()
                this[GridConnectionTable.numVanChargePoints] = gridConnection.transport.vans.numChargePoints?.toUInt()
                this[GridConnectionTable.powerPerVanChargePointKw] = gridConnection.transport.vans.powerPerChargePointKw
                this[GridConnectionTable.annualTravelDistancePerVanKm] = gridConnection.transport.vans.annualTravelDistancePerVanKm?.toUInt()
                this[GridConnectionTable.numPlannedElectricVans] = gridConnection.transport.vans.numPlannedElectricVans?.toUInt()
                this[GridConnectionTable.numPlannedHydgrogenVans] = gridConnection.transport.vans.numPlannedHydrogenVans?.toUInt()

                this[GridConnectionTable.numCars] = gridConnection.transport.cars.numCars?.toUInt()
                this[GridConnectionTable.numElectricCars] = gridConnection.transport.cars.numElectricCars?.toUInt()
                this[GridConnectionTable.numCarChargePoints] = gridConnection.transport.cars.numChargePoints?.toUInt()
                this[GridConnectionTable.powerPerCarChargePointKw] = gridConnection.transport.cars.powerPerChargePointKw
                this[GridConnectionTable.annualTravelDistancePerCarKm] = gridConnection.transport.cars.annualTravelDistancePerCarKm?.toUInt()
                this[GridConnectionTable.numPlannedElectricCars] = gridConnection.transport.cars.numPlannedElectricCars?.toUInt()
                this[GridConnectionTable.numPlannedHydgrogenCars] = gridConnection.transport.cars.numPlannedHydrogenCars?.toUInt()

                // electricity
                this[GridConnectionTable.electricityEan] = gridConnection.electricity.ean
                this[GridConnectionTable.annualElectricityDemandKwh] = gridConnection.electricity.annualElectricityDemandKwh?.toUInt()
                this[GridConnectionTable.annualElectricityProductionKwh] = gridConnection.electricity.annualElectricityProductionKwh?.toUInt()
                this[GridConnectionTable.kleinverbruikElectricityConnectionCapacity] = gridConnection.electricity.kleinverbruik?.connectionCapacity
                this[GridConnectionTable.kleinverbuikElectricityConsumptionProfile] = gridConnection.electricity.kleinverbruik?.consumptionProfile
                this[GridConnectionTable.grootverbruikContractedDemandCapacityKw] = gridConnection.electricity.grootverbruik?.contractedConnectionDemandCapacityKw?.toUInt()
                this[GridConnectionTable.grootverbruikContractedSupplyCapacityKw] = gridConnection.electricity.grootverbruik?.contractedConnectionSupplyCapacityKw?.toUInt()

                // supply
                this[GridConnectionTable.hasSupply] = gridConnection.supply.hasSupply
                this[GridConnectionTable.pvInstalledKwp] = gridConnection.supply.pvInstalledKwp?.toUInt()
                this[GridConnectionTable.pvOrientation] = gridConnection.supply.pvOrientation
                this[GridConnectionTable.pvPlanned] = gridConnection.supply.pvPlanned
                this[GridConnectionTable.pvPlannedKwp] = gridConnection.supply.pvPlannedKwp?.toUInt()
                this[GridConnectionTable.pvPlannedOrientation] = gridConnection.supply.pvPlannedOrientation
                this[GridConnectionTable.pvPlannedYear] = gridConnection.supply.pvPlannedYear?.toUInt()
                this[GridConnectionTable.missingPvReason] = gridConnection.supply.missingPvReason
                this[GridConnectionTable.windInstalledKw] = gridConnection.supply.windInstalledKw
                this[GridConnectionTable.otherSupply] = gridConnection.supply.otherSupply

                // natural gas
                this[GridConnectionTable.hasNaturalGasConnection] = gridConnection.naturalGas.hasConnection
                this[GridConnectionTable.naturalGasEan] = gridConnection.naturalGas.ean
                this[GridConnectionTable.naturalGasAnnualDemandM3] = gridConnection.naturalGas.annualDemandM3?.toUInt()
                this[GridConnectionTable.percentageNaturalGasForHeating] = gridConnection.naturalGas.percentageUsedForHeating?.toUInt()

                // heat
                this[GridConnectionTable.heatingTypes] = gridConnection.heat.heatingTypes
                this[GridConnectionTable.sumGasBoilerKw] = gridConnection.heat.sumGasBoilerKw
                this[GridConnectionTable.sumHeatPumpKw] = gridConnection.heat.sumHeatPumpKw
                this[GridConnectionTable.sumHybridHeatPumpElectricKw] = gridConnection.heat.sumHybridHeatPumpElectricKw
                this[GridConnectionTable.annualDistrictHeatingDemandGj] = gridConnection.heat.annualDistrictHeatingDemandGj
                this[GridConnectionTable.localHeatExchangeDescription] = gridConnection.heat.localHeatExchangeDescription
                this[GridConnectionTable.hasUnusedResidualHeat] = gridConnection.heat.hasUnusedResidualHeat

                // storage
                this[GridConnectionTable.hasBattery] = gridConnection.storage.hasBattery
                this[GridConnectionTable.batteryCapacityKwh] = gridConnection.storage.batteryCapacityKwh
                this[GridConnectionTable.batteryPowerKw] = gridConnection.storage.batteryPowerKw
                this[GridConnectionTable.batterySchedule] = gridConnection.storage.batterySchedule
                this[GridConnectionTable.hasPlannedBattery] = gridConnection.storage.hasPlannedBattery
                this[GridConnectionTable.plannedBatteryCapacityKwh] = gridConnection.storage.plannedBatteryCapacityKwh
                this[GridConnectionTable.plannedBatteryPowerKw] = gridConnection.storage.plannedBatteryPowerKw
                this[GridConnectionTable.plannedBatterySchedule] = gridConnection.storage.plannedBatterySchedule
                this[GridConnectionTable.hasThermalStorage] = gridConnection.storage.hasThermalStorage
            }

            for (address in survey.addresses) {
                for (gridConnection in address.gridConnections) {
                    for (electricityFile in gridConnection.electricity.quarterHourlyValuesFiles) {
                        FileTable.insert {
                            it[gridConnectionId] = gridConnection.id
                            it[purpose] = BlobPurpose.ELECTRICITY_VALUES
                            it[blobName] = electricityFile.blobName
                            it[originalName] = electricityFile.originalName
                            it[size] = electricityFile.size
                            it[contentType] = electricityFile.contentType
                        }
                    }

                    val authorizationFile = gridConnection.electricity.authorizationFile
                    if (authorizationFile != null) {
                        FileTable.insert {
                            it[gridConnectionId] = gridConnection.id
                            it[purpose] = BlobPurpose.ELECTRICITY_AUTHORIZATION
                            it[blobName] = authorizationFile.blobName
                            it[originalName] = authorizationFile.originalName
                            it[size] = authorizationFile.size
                            it[contentType] = authorizationFile.contentType
                        }
                    }

                    for (gasFile in gridConnection.naturalGas.hourlyValuesFiles) {
                        FileTable.insert {
                            it[gridConnectionId] = gridConnection.id
                            it[purpose] = BlobPurpose.NATURAL_GAS_VALUES
                            it[blobName] = gasFile.blobName
                            it[originalName] = gasFile.originalName
                            it[size] = gasFile.size
                            it[contentType] = gasFile.contentType
                        }
                    }
                }
            }
        }

        return surveyId
    }
}