package com.zenmo.companysurvey

import com.zenmo.blob.BlobPurpose
import com.zenmo.companysurvey.dto.GridConnection
import com.zenmo.companysurvey.dto.Survey
import com.zenmo.companysurvey.table.AddressTable
import com.zenmo.companysurvey.table.CompanySurveyGridConnectionTable
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

            CompanySurveyGridConnectionTable.batchInsert(survey.addresses.flatMap { address ->
                address.gridConnections.map { gridConnection ->
                    Pair(
                        address.id,
                        gridConnection,
                    )
                }
            }) { pair: Pair<UUID, GridConnection> ->
                val (addressId, gridConnection) = pair

                this[CompanySurveyGridConnectionTable.id] = gridConnection.id
                this[CompanySurveyGridConnectionTable.addressId] = addressId

                // open questions
                this[CompanySurveyGridConnectionTable.energyOrBuildingManagementSystemSupplier] = gridConnection.energyOrBuildingManagementSystemSupplier
                this[CompanySurveyGridConnectionTable.mainConsumptionProcess] = gridConnection.mainConsumptionProcess
                this[CompanySurveyGridConnectionTable.consumptionFlexibility] = gridConnection.consumptionFlexibility
                this[CompanySurveyGridConnectionTable.electrificationPlans] = gridConnection.electrificationPlans
                this[CompanySurveyGridConnectionTable.surveyFeedback] = gridConnection.surveyFeedback

                this[CompanySurveyGridConnectionTable.hasVehicles] = gridConnection.transport.hasVehicles
                this[CompanySurveyGridConnectionTable.numDailyCarCommuters] = gridConnection.transport.numDailyCarCommuters?.toUInt()

                this[CompanySurveyGridConnectionTable.numTrucks] = gridConnection.transport.trucks.numTrucks?.toUInt()
                this[CompanySurveyGridConnectionTable.numElectricTrucks] = gridConnection.transport.trucks.numElectricTrucks?.toUInt()
                this[CompanySurveyGridConnectionTable.numTruckChargePoints] = gridConnection.transport.trucks.numChargePoints?.toUInt()
                this[CompanySurveyGridConnectionTable.powerPerTruckChargePointKw] = gridConnection.transport.trucks.powerPerChargePointKw
                this[CompanySurveyGridConnectionTable.annualTravelDistancePerTruckKm] = gridConnection.transport.trucks.annualTravelDistancePerTruckKm?.toUInt()
                this[CompanySurveyGridConnectionTable.numPlannedElectricTrucks] = gridConnection.transport.trucks.numPlannedElectricTrucks?.toUInt()

                this[CompanySurveyGridConnectionTable.numVans] = gridConnection.transport.vans.numVans?.toUInt()
                this[CompanySurveyGridConnectionTable.numElectricVans] = gridConnection.transport.vans.numElectricVans?.toUInt()
                this[CompanySurveyGridConnectionTable.numVanChargePoints] = gridConnection.transport.vans.numChargePoints?.toUInt()
                this[CompanySurveyGridConnectionTable.powerPerVanChargePointKw] = gridConnection.transport.vans.powerPerChargePointKw
                this[CompanySurveyGridConnectionTable.annualTravelDistancePerVanKm] = gridConnection.transport.vans.annualTravelDistancePerVanKm?.toUInt()
                this[CompanySurveyGridConnectionTable.numPlannedElectricVans] = gridConnection.transport.vans.numPlannedElectricVans?.toUInt()

                this[CompanySurveyGridConnectionTable.numCars] = gridConnection.transport.cars.numCars?.toUInt()
                this[CompanySurveyGridConnectionTable.numElectricCars] = gridConnection.transport.cars.numElectricCars?.toUInt()
                this[CompanySurveyGridConnectionTable.numCarChargePoints] = gridConnection.transport.cars.numChargePoints?.toUInt()
                this[CompanySurveyGridConnectionTable.powerPerCarChargePointKw] = gridConnection.transport.cars.powerPerChargePointKw
                this[CompanySurveyGridConnectionTable.annualTravelDistancePerCarKm] = gridConnection.transport.cars.annualTravelDistancePerCarKm?.toUInt()
                this[CompanySurveyGridConnectionTable.numPlannedElectricCars] = gridConnection.transport.cars.numPlannedElectricCars?.toUInt()

                // electricity
                this[CompanySurveyGridConnectionTable.electricityEan] = gridConnection.electricity.ean
                this[CompanySurveyGridConnectionTable.annualElectricityDemandKwh] = gridConnection.electricity.annualElectricityDemandKwh?.toUInt()
                this[CompanySurveyGridConnectionTable.annualElectricityProductionKwh] = gridConnection.electricity.annualElectricityProductionKwh?.toUInt()
                this[CompanySurveyGridConnectionTable.kleinverbruikElectricityConnectionCapacity] = gridConnection.electricity.kleinverbruik?.connectionCapacity
                this[CompanySurveyGridConnectionTable.kleinverbuikElectricityConsumptionProfile] = gridConnection.electricity.kleinverbruik?.consumptionProfile
                this[CompanySurveyGridConnectionTable.grootverbruikContractedDemandCapacityKw] = gridConnection.electricity.grootverbruik?.contractedConnectionDemandCapacityKw?.toUInt()
                this[CompanySurveyGridConnectionTable.grootverbruikContractedSupplyCapacityKw] = gridConnection.electricity.grootverbruik?.contractedConnectionSupplyCapacityKw?.toUInt()

                // supply
                this[CompanySurveyGridConnectionTable.hasSupply] = gridConnection.supply.hasSupply
                this[CompanySurveyGridConnectionTable.pvInstalledKwp] = gridConnection.supply.pvInstalledKwp?.toUInt()
                this[CompanySurveyGridConnectionTable.pvOrientation] = gridConnection.supply.pvOrientation
                this[CompanySurveyGridConnectionTable.pvPlanned] = gridConnection.supply.pvPlanned
                this[CompanySurveyGridConnectionTable.pvPlannedKwp] = gridConnection.supply.pvPlannedKwp?.toUInt()
                this[CompanySurveyGridConnectionTable.pvPlannedOrientation] = gridConnection.supply.pvPlannedOrientation
                this[CompanySurveyGridConnectionTable.pvPlannedYear] = gridConnection.supply.pvPlannedYear?.toUInt()
                this[CompanySurveyGridConnectionTable.windInstalledKw] = gridConnection.supply.windInstalledKw
                this[CompanySurveyGridConnectionTable.otherSupply] = gridConnection.supply.otherSupply

                // natural gas
                this[CompanySurveyGridConnectionTable.hasNaturalGasConnection] = gridConnection.naturalGas.hasConnection
                this[CompanySurveyGridConnectionTable.naturalGasEan] = gridConnection.naturalGas.ean
                this[CompanySurveyGridConnectionTable.naturalGasAnnualDemandM3] = gridConnection.naturalGas.annualDemandM3?.toUInt()
                this[CompanySurveyGridConnectionTable.percentageNaturalGasForHeating] = gridConnection.naturalGas.percentageUsedForHeating?.toUInt()

                // heat
                this[CompanySurveyGridConnectionTable.heatingTypes] = gridConnection.heat.heatingTypes
                this[CompanySurveyGridConnectionTable.sumGasBoilerKw] = gridConnection.heat.sumGasBoilerKw
                this[CompanySurveyGridConnectionTable.sumHeatPumpKw] = gridConnection.heat.sumHeatPumpKw
                this[CompanySurveyGridConnectionTable.sumHybridHeatPumpElectricKw] = gridConnection.heat.sumHybridHeatPumpElectricKw
                this[CompanySurveyGridConnectionTable.annualDistrictHeatingDemandGj] = gridConnection.heat.annualDistrictHeatingDemandGj
                this[CompanySurveyGridConnectionTable.localHeatExchangeDescription] = gridConnection.heat.localHeatExchangeDescription
                this[CompanySurveyGridConnectionTable.hasUnusedResidualHeat] = gridConnection.heat.hasUnusedResidualHeat

                // storage
                this[CompanySurveyGridConnectionTable.hasBattery] = gridConnection.storage.hasBattery
                this[CompanySurveyGridConnectionTable.batteryCapacityKwh] = gridConnection.storage.batteryCapacityKwh
                this[CompanySurveyGridConnectionTable.batteryPowerKw] = gridConnection.storage.batteryPowerKw
                this[CompanySurveyGridConnectionTable.batterySchedule] = gridConnection.storage.batterySchedule
                this[CompanySurveyGridConnectionTable.hasPlannedBattery] = gridConnection.storage.hasPlannedBattery
                this[CompanySurveyGridConnectionTable.plannedBatteryCapacityKwh] = gridConnection.storage.plannedBatteryCapacityKwh
                this[CompanySurveyGridConnectionTable.plannedBatteryPowerKw] = gridConnection.storage.plannedBatteryPowerKw
                this[CompanySurveyGridConnectionTable.plannedBatterySchedule] = gridConnection.storage.plannedBatterySchedule
                this[CompanySurveyGridConnectionTable.hasThermalStorage] = gridConnection.storage.hasThermalStorage
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