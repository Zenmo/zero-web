package com.zenmo.orm.companysurvey

import com.zenmo.orm.blob.BlobPurpose
import com.zenmo.orm.companysurvey.dto.*
import com.zenmo.orm.companysurvey.table.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.ResultSet
import java.util.*

class SurveyRepository(
        private val db: Database
) {
    fun getHessenpoortSurveys(): List<Survey> {
        return getSurveyByProject("Hessenpoort")
    }

    fun getDeWiekenSurveys(): List<Survey> {
        return getSurveyByProject("De Wieken")
    }

    fun getSurveyByProject(project: String): List<Survey> {
        return getSurveys(CompanySurveyTable.project eq project)
    }

    fun getSurveys(filter: Op<Boolean> = Op.TRUE): List<Survey> {
        return transaction(db) {
            val surveysWithoutAddresses = CompanySurveyTable.selectAll()
                .where {
                    filter
                }
                .map { hydrateSurvey(it) }

            val addressRows = AddressTable.selectAll()
                .where {
                    AddressTable.surveyId inList surveysWithoutAddresses.map { it.id }
                }
                .toList()

            val gridConnectionRows = GridConnectionTable.selectAll()
                .where {
                    GridConnectionTable.addressId inList addressRows.map { it[AddressTable.id] }
                }
                .toList()

            val surveyIdToAddressWithoutGridConnections = addressRows
                .groupBy { it[AddressTable.surveyId] }
                .mapValues {
                    it.value.map { hydrateAddress(it) }
                }

            val addressIdToGridConnectionWithoutFiles: Map<UUID, List<GridConnection>> = gridConnectionRows
                .groupBy { it[GridConnectionTable.addressId] }
                .mapValues {
                    it.value.map { hydrateGridConnection(it) }
                }

            val filesPerPurpose: Map<Pair<BlobPurpose, UUID>, List<File>> = FileTable.selectAll()
                .where {
                    FileTable.gridConnectionId inList gridConnectionRows.map { it[GridConnectionTable.id] }
                }
                .toList()
                .groupBy { Pair(it[FileTable.purpose], it[FileTable.gridConnectionId]) }
                .mapValues {
                    it.value.map { hydrateFile(it) }
                }

            val addressIdToGridConnection = addressIdToGridConnectionWithoutFiles
                .mapValues { gridConnections ->
                    gridConnections.value.map { gridConnection ->
                        val electricityFiles = filesPerPurpose.getOrDefault(
                            Pair(BlobPurpose.ELECTRICITY_VALUES, gridConnection.id),
                            emptyList()
                        )
                        val authorizationFile =
                            filesPerPurpose.get(Pair(BlobPurpose.ELECTRICITY_AUTHORIZATION, gridConnection.id))
                                ?.firstOrNull()
                        val gasFiles = filesPerPurpose.getOrDefault(
                            Pair(BlobPurpose.NATURAL_GAS_VALUES, gridConnection.id),
                            emptyList()
                        )

                        gridConnection.copy(
                            electricity = gridConnection.electricity.copy(
                                quarterHourlyValuesFiles = electricityFiles,
                                authorizationFile = authorizationFile,
                            ),
                            naturalGas = gridConnection.naturalGas.copy(
                                hourlyValuesFiles = gasFiles,
                            ),
                        )
                    }
                }

            val surveyIdToAddress = surveyIdToAddressWithoutGridConnections
                .mapValues {
                    it.value.map { address ->
                        address.copy(
                            gridConnections = addressIdToGridConnection.getOrDefault(address.id, emptyList())
                        )
                    }
                }

            surveysWithoutAddresses.map {
                it.copy(
                    addresses = surveyIdToAddress.getOrDefault(it.id, emptyList())
                )
            }
        }
    }

    protected fun hydrateSurvey(row: ResultRow): Survey {
        return Survey(
            id = row[CompanySurveyTable.id],
            created = row[CompanySurveyTable.created],
            zenmoProject = row[CompanySurveyTable.project],
            companyName = row[CompanySurveyTable.companyName],
            personName = row[CompanySurveyTable.personName],
            email = row[CompanySurveyTable.email],
            dataSharingAgreed = row[CompanySurveyTable.dataSharingAgreed],
            addresses = emptyList(), // data from different table
        )
    }

    protected fun hydrateAddress(row: ResultRow): Address {
        return Address(
            id = row[AddressTable.id],
            street = row[AddressTable.street],
            houseNumber = row[AddressTable.houseNumber].toInt(),
            houseNumberSuffix = row[AddressTable.houseNumberSuffix],
            houseLetter = row[AddressTable.houseLetter],
            postalCode = row[AddressTable.postalCode],
            city = row[AddressTable.city],
            gridConnections = emptyList(), // data from different table
        )
    }

    protected fun hydrateFile(row: ResultRow): File {
        return File(
            blobName = row[FileTable.blobName],
            originalName = row[FileTable.originalName],
            size = row[FileTable.size],
            contentType = row[FileTable.contentType],
        )
    }

    /**
     * Note that files are missing
     */
    protected fun hydrateGridConnection(row: ResultRow): GridConnection {
        return GridConnection(
            id = row[GridConnectionTable.id],
            sequence = row[GridConnectionTable.sequence],
            energyOrBuildingManagementSystemSupplier = row[GridConnectionTable.energyOrBuildingManagementSystemSupplier],
            mainConsumptionProcess = row[GridConnectionTable.mainConsumptionProcess],
            consumptionFlexibility = row[GridConnectionTable.consumptionFlexibility],
            expansionPlans = row[GridConnectionTable.expansionPlans],
            electrificationPlans = row[GridConnectionTable.electrificationPlans],
            surveyFeedback = row[GridConnectionTable.surveyFeedback],
            electricity = Electricity(
                hasConnection = row[GridConnectionTable.hasElectricityConnection],
                ean = row[GridConnectionTable.electricityEan],
                annualElectricityDemandKwh = row[GridConnectionTable.annualElectricityDemandKwh]?.toInt(),
                annualElectricityProductionKwh = row[GridConnectionTable.annualElectricityProductionKwh]?.toInt(),
                kleinverbruik = CompanyKleinverbruik(
                    connectionCapacity = row[GridConnectionTable.kleinverbruikElectricityConnectionCapacity]?.let {
                        KleinverbruikElectricityConnectionCapacity.valueOf(
                            it.name
                        )
                    },
                    consumptionProfile = row[GridConnectionTable.kleinverbuikElectricityConsumptionProfile]?.let {
                        KleinverbruikElectricityConsumptionProfile.valueOf(
                            it.name
                        )
                    },
                ),
                grootverbruik = CompanyGrootverbruik(
                    contractedConnectionDemandCapacityKw = row[GridConnectionTable.grootverbruikContractedDemandCapacityKw]?.toInt(),
                    contractedConnectionSupplyCapacityKw = row[GridConnectionTable.grootverbruikContractedSupplyCapacityKw]?.toInt(),
                    physicalCapacityKw = row[GridConnectionTable.grootverbruikPhysicalCapacityKw]?.toInt(),
                ),
                gridExpansion = GridExpansion(
                    hasRequestAtGridOperator = row[GridConnectionTable.hasExpansionRequestAtGridOperator],
                    requestedKW = row[GridConnectionTable.expansionRequestKW]?.toInt(),
                    reason = row[GridConnectionTable.expansionRequestReason],
                ),
            ),
            supply = Supply(
                hasSupply = row[GridConnectionTable.hasSupply],
                pvInstalledKwp = row[GridConnectionTable.pvInstalledKwp]?.toInt(),
                pvOrientation = row[GridConnectionTable.pvOrientation]?.let { PVOrientation.valueOf(it.name) },
                pvPlanned = row[GridConnectionTable.pvPlanned],
                pvPlannedKwp = row[GridConnectionTable.pvPlannedKwp]?.toInt(),
                pvPlannedOrientation = row[GridConnectionTable.pvPlannedOrientation]?.let { PVOrientation.valueOf(it.name) },
                pvPlannedYear = row[GridConnectionTable.pvPlannedYear]?.toInt(),
                missingPvReason = row[GridConnectionTable.missingPvReason],
                windInstalledKw = row[GridConnectionTable.windInstalledKw],
                windPlannedKw = row[GridConnectionTable.windPlannedKw],
                otherSupply = row[GridConnectionTable.otherSupply],
            ),
            naturalGas = NaturalGas(
                hasConnection = row[GridConnectionTable.hasNaturalGasConnection],
                ean = row[GridConnectionTable.naturalGasEan],
                annualDemandM3 = row[GridConnectionTable.naturalGasAnnualDemandM3]?.toInt(),
                percentageUsedForHeating = row[GridConnectionTable.percentageNaturalGasForHeating]?.toInt(),
            ),
            heat = Heat(
                heatingTypes = row[GridConnectionTable.heatingTypes],
                sumGasBoilerKw = row[GridConnectionTable.sumGasBoilerKw],
                sumHeatPumpKw = row[GridConnectionTable.sumHeatPumpKw],
                sumHybridHeatPumpElectricKw = row[GridConnectionTable.sumHybridHeatPumpElectricKw],
                annualDistrictHeatingDemandGj = row[GridConnectionTable.annualDistrictHeatingDemandGj],
                localHeatExchangeDescription = row[GridConnectionTable.localHeatExchangeDescription],
                hasUnusedResidualHeat = row[GridConnectionTable.hasUnusedResidualHeat],
            ),
            storage = Storage(
                hasBattery = row[GridConnectionTable.hasBattery],
                batteryCapacityKwh = row[GridConnectionTable.batteryCapacityKwh],
                batteryPowerKw = row[GridConnectionTable.batteryPowerKw],
                batterySchedule = row[GridConnectionTable.batterySchedule],
                hasPlannedBattery = row[GridConnectionTable.hasPlannedBattery],
                plannedBatteryCapacityKwh = row[GridConnectionTable.plannedBatteryCapacityKwh],
                plannedBatteryPowerKw = row[GridConnectionTable.plannedBatteryPowerKw],
                plannedBatterySchedule = row[GridConnectionTable.plannedBatterySchedule],
                hasThermalStorage = row[GridConnectionTable.hasThermalStorage],
                thermalStorageKw = row[GridConnectionTable.thermalStorageKw],
            ),
            transport = Transport(
                hasVehicles = row[GridConnectionTable.hasVehicles],
                numDailyCarAndVanCommuters = row[GridConnectionTable.numDailyCarAndVanCommuters]?.toInt(),
                numDailyCarVisitors = row[GridConnectionTable.numDailyCarVisitors]?.toInt(),
                numCommuterAndVisitorChargePoints = row[GridConnectionTable.numCommuterAndVisitorChargePoints]?.toInt(),
                trucks = Trucks(
                    numTrucks = row[GridConnectionTable.numTrucks]?.toInt(),
                    numElectricTrucks = row[GridConnectionTable.numElectricTrucks]?.toInt(),
                    numChargePoints = row[GridConnectionTable.numTruckChargePoints]?.toInt(),
                    powerPerChargePointKw = row[GridConnectionTable.powerPerTruckChargePointKw],
                    annualTravelDistancePerTruckKm = row[GridConnectionTable.annualTravelDistancePerTruckKm]?.toInt(),
                    numPlannedElectricTrucks = row[GridConnectionTable.numPlannedElectricTrucks]?.toInt(),
                    numPlannedHydrogenTrucks = row[GridConnectionTable.numPlannedHydgrogenTrucks]?.toInt(),
                ),
                vans = Vans(
                    numVans = row[GridConnectionTable.numVans]?.toInt(),
                    numElectricVans = row[GridConnectionTable.numElectricVans]?.toInt(),
                    numChargePoints = row[GridConnectionTable.numVanChargePoints]?.toInt(),
                    powerPerChargePointKw = row[GridConnectionTable.powerPerVanChargePointKw],
                    annualTravelDistancePerVanKm = row[GridConnectionTable.annualTravelDistancePerVanKm]?.toInt(),
                    numPlannedElectricVans = row[GridConnectionTable.numPlannedElectricVans]?.toInt(),
                    numPlannedHydrogenVans = row[GridConnectionTable.numPlannedHydgrogenVans]?.toInt(),
                ),
                cars = Cars(
                    numCars = row[GridConnectionTable.numCars]?.toInt(),
                    numElectricCars = row[GridConnectionTable.numElectricCars]?.toInt(),
                    numChargePoints = row[GridConnectionTable.numCarChargePoints]?.toInt(),
                    powerPerChargePointKw = row[GridConnectionTable.powerPerCarChargePointKw],
                    annualTravelDistancePerCarKm = row[GridConnectionTable.annualTravelDistancePerCarKm]?.toInt(),
                    numPlannedElectricCars = row[GridConnectionTable.numPlannedElectricCars]?.toInt(),
                    numPlannedHydrogenCars = row[GridConnectionTable.numPlannedHydgrogenCars]?.toInt(),
                ),
                otherVehicles = OtherVehicles(
                    hasOtherVehicles = row[GridConnectionTable.hasOtherVehicles],
                    description = row[GridConnectionTable.otherVehiclesDescription],
                ),
            ),
        )
    }

    fun save(survey: Survey): UUID {
        transaction(db) {
            CompanySurveyTable.insert {
                it[id] = survey.id
                it[created] = survey.created
                it[project] = survey.zenmoProject
                it[companyName] = survey.companyName
                it[personName] = survey.personName
                it[email] = survey.email
                it[dataSharingAgreed] = survey.dataSharingAgreed
            }

            AddressTable.batchInsert(survey.addresses) {
                address ->
                this[AddressTable.id] = address.id
                this[AddressTable.surveyId] = survey.id
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
                this[GridConnectionTable.expansionPlans] = gridConnection.expansionPlans
                this[GridConnectionTable.electrificationPlans] = gridConnection.electrificationPlans
                this[GridConnectionTable.surveyFeedback] = gridConnection.surveyFeedback

                // transport
                this[GridConnectionTable.hasVehicles] = gridConnection.transport.hasVehicles
                this[GridConnectionTable.numDailyCarAndVanCommuters] = gridConnection.transport.numDailyCarAndVanCommuters?.toUInt()
                this[GridConnectionTable.numDailyCarVisitors] = gridConnection.transport.numDailyCarVisitors?.toUInt()
                this[GridConnectionTable.numCommuterAndVisitorChargePoints] = gridConnection.transport.numCommuterAndVisitorChargePoints?.toUInt()

                // trucks
                this[GridConnectionTable.numTrucks] = gridConnection.transport.trucks.numTrucks?.toUInt()
                this[GridConnectionTable.numElectricTrucks] = gridConnection.transport.trucks.numElectricTrucks?.toUInt()
                this[GridConnectionTable.numTruckChargePoints] = gridConnection.transport.trucks.numChargePoints?.toUInt()
                this[GridConnectionTable.powerPerTruckChargePointKw] = gridConnection.transport.trucks.powerPerChargePointKw
                this[GridConnectionTable.annualTravelDistancePerTruckKm] = gridConnection.transport.trucks.annualTravelDistancePerTruckKm?.toUInt()
                this[GridConnectionTable.numPlannedElectricTrucks] = gridConnection.transport.trucks.numPlannedElectricTrucks?.toUInt()
                this[GridConnectionTable.numPlannedHydgrogenTrucks] = gridConnection.transport.trucks.numPlannedHydrogenTrucks?.toUInt()

                // vans
                this[GridConnectionTable.numVans] = gridConnection.transport.vans.numVans?.toUInt()
                this[GridConnectionTable.numElectricVans] = gridConnection.transport.vans.numElectricVans?.toUInt()
                this[GridConnectionTable.numVanChargePoints] = gridConnection.transport.vans.numChargePoints?.toUInt()
                this[GridConnectionTable.powerPerVanChargePointKw] = gridConnection.transport.vans.powerPerChargePointKw
                this[GridConnectionTable.annualTravelDistancePerVanKm] = gridConnection.transport.vans.annualTravelDistancePerVanKm?.toUInt()
                this[GridConnectionTable.numPlannedElectricVans] = gridConnection.transport.vans.numPlannedElectricVans?.toUInt()
                this[GridConnectionTable.numPlannedHydgrogenVans] = gridConnection.transport.vans.numPlannedHydrogenVans?.toUInt()

                // cars
                this[GridConnectionTable.numCars] = gridConnection.transport.cars.numCars?.toUInt()
                this[GridConnectionTable.numElectricCars] = gridConnection.transport.cars.numElectricCars?.toUInt()
                this[GridConnectionTable.numCarChargePoints] = gridConnection.transport.cars.numChargePoints?.toUInt()
                this[GridConnectionTable.powerPerCarChargePointKw] = gridConnection.transport.cars.powerPerChargePointKw
                this[GridConnectionTable.annualTravelDistancePerCarKm] = gridConnection.transport.cars.annualTravelDistancePerCarKm?.toUInt()
                this[GridConnectionTable.numPlannedElectricCars] = gridConnection.transport.cars.numPlannedElectricCars?.toUInt()
                this[GridConnectionTable.numPlannedHydgrogenCars] = gridConnection.transport.cars.numPlannedHydrogenCars?.toUInt()

                // other vehicles
                this[GridConnectionTable.hasOtherVehicles] = gridConnection.transport.otherVehicles.hasOtherVehicles
                this[GridConnectionTable.otherVehiclesDescription] = gridConnection.transport.otherVehicles.description

                // electricity
                this[GridConnectionTable.hasElectricityConnection] = gridConnection.electricity.hasConnection
                this[GridConnectionTable.electricityEan] = gridConnection.electricity.ean
                this[GridConnectionTable.annualElectricityDemandKwh] = gridConnection.electricity.annualElectricityDemandKwh?.toUInt()
                this[GridConnectionTable.annualElectricityProductionKwh] = gridConnection.electricity.annualElectricityProductionKwh?.toUInt()
                this[GridConnectionTable.kleinverbruikElectricityConnectionCapacity] = gridConnection.electricity.kleinverbruik?.connectionCapacity
                this[GridConnectionTable.kleinverbuikElectricityConsumptionProfile] = gridConnection.electricity.kleinverbruik?.consumptionProfile
                this[GridConnectionTable.grootverbruikContractedDemandCapacityKw] = gridConnection.electricity.grootverbruik?.contractedConnectionDemandCapacityKw?.toUInt()
                this[GridConnectionTable.grootverbruikContractedSupplyCapacityKw] = gridConnection.electricity.grootverbruik?.contractedConnectionSupplyCapacityKw?.toUInt()
                this[GridConnectionTable.grootverbruikPhysicalCapacityKw] = gridConnection.electricity.grootverbruik?.physicalCapacityKw?.toUInt()
                this[GridConnectionTable.hasExpansionRequestAtGridOperator] = gridConnection.electricity.gridExpansion.hasRequestAtGridOperator
                this[GridConnectionTable.expansionRequestKW] = gridConnection.electricity.gridExpansion.requestedKW?.toUInt()
                this[GridConnectionTable.expansionRequestReason] = gridConnection.electricity.gridExpansion.reason

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
                this[GridConnectionTable.windPlannedKw] = gridConnection.supply.windPlannedKw
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
                this[GridConnectionTable.thermalStorageKw] = gridConnection.storage.thermalStorageKw
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

        return survey.id
    }
}