package com.zenmo.orm.companysurvey.table

import com.zenmo.orm.companysurvey.dto.*
import com.zenmo.orm.dbutil.PGEnum
import com.zenmo.orm.dbutil.enumArray
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Sequence
import org.jetbrains.exposed.sql.nextIntVal

/**
 * This is to have a human-readable id for grid connections.
 */
val gridConnectionSequence = Sequence("grid_connection_sequence")

object GridConnectionTable: Table("grid_connection") {
    val id = uuid("id").autoGenerate()
    override val primaryKey = PrimaryKey(id)
    val addressId = uuid("address_id").references(AddressTable.id)

    val sequence = integer("sequence").defaultExpression(gridConnectionSequence.nextIntVal())

    /**
     * Open questions
     */
    val energyOrBuildingManagementSystemSupplier = varchar("energy_or_building_management_system_supplier", 1000)
    val mainConsumptionProcess = varchar("main_consumption_process", 1000)
    val consumptionFlexibility = varchar("consumption_flexibility", 1000)
    val expansionPlans = varchar("expansion_plans", 1000)
    val electrificationPlans = varchar("electrification_plans", 1000)
    val surveyFeedback = varchar("survey_feedback", 1000)

    /**
     * [com.zenmo.companysurvey.dto.Electricity]
     */
    val hasElectricityConnection = bool("has_electricity_connection").nullable()
    val electricityEan = varchar("electricity_ean", 18)
    val annualElectricityDemandKwh = uinteger("annual_electricity_demand_kwh").nullable()
    val annualElectricityProductionKwh = uinteger("annual_electricity_production_kwh").nullable()

    val kleinverbruikElectricityConnectionCapacity = customEnumeration(
        "kleinverbuik_electricity_connection_capacity",
        KleinverbruikElectricityConnectionCapacity::class.simpleName,
        fromDb = { KleinverbruikElectricityConnectionCapacity.valueOf(it as String) },
        toDb = { PGEnum(KleinverbruikElectricityConnectionCapacity::class.simpleName!!, it) }).nullable()

    val kleinverbuikElectricityConsumptionProfile = customEnumeration(
        "kleinverbuik_electricity_consumption_profile",
        KleinverbruikElectricityConsumptionProfile::class.simpleName,
        fromDb = { KleinverbruikElectricityConsumptionProfile.valueOf(it as String) },
        toDb = { PGEnum(KleinverbruikElectricityConsumptionProfile::class.simpleName!!, it) }).nullable()

    val grootverbruikContractedDemandCapacityKw = uinteger("grootverbruik_contracted_demand_capacity_kw").nullable()
    val grootverbruikContractedSupplyCapacityKw = uinteger("grootverbruik_contracted_supply_capacity_kw").nullable()
    val grootverbruikPhysicalCapacityKw = uinteger("grootverbruik_physical_capacity_kw").nullable()
    val hasExpansionRequestAtGridOperator = bool("has_expansion_request_at_grid_operator").nullable()
    val expansionRequestKW = uinteger("expansion_request_kw").nullable()
    val expansionRequestReason = varchar("expansion_request_reason", 1000)

    /**
     * [com.zenmo.companysurvey.dto.Supply]
     */
    val hasSupply = bool("has_supply").nullable()
    val pvInstalledKwp = uinteger("pv_installed_kwp").nullable()
    val pvOrientation = customEnumeration(
        "pv_orientation",
        PVOrientation::class.simpleName,
        fromDb = { PVOrientation.valueOf(it as String) },
        toDb = { PGEnum(PVOrientation::class.simpleName!!, it) }).nullable()
    val pvPlanned = bool("pv_planned").nullable()
    val pvPlannedKwp = uinteger("pv_planned_kwp").nullable()
    val pvPlannedOrientation = customEnumeration(
        "pv_planned_orientation",
        PVOrientation::class.simpleName,
        fromDb = { PVOrientation.valueOf(it as String) },
        toDb = { PGEnum(PVOrientation::class.simpleName!!, it) }).nullable()
    val pvPlannedYear = uinteger("pv_planned_year").nullable()
    val missingPvReason = customEnumeration(
        "missing_pv_reason",
        MissingPvReason::class.simpleName,
        fromDb = { MissingPvReason.valueOf(it as String) },
        toDb = { PGEnum(MissingPvReason::class.simpleName!!, it) }).nullable()
    val windInstalledKw = float("wind_installed_kw").nullable()
    val windPlannedKw = float("wind_planned_kw").nullable()
    val otherSupply = varchar("other_supply", 1000)

    /**
     * [com.zenmo.companysurvey.dto.NaturalGas]
     */
    val hasNaturalGasConnection = bool("has_natural_gas_connection").nullable()
    val naturalGasEan = varchar("natural_gas_ean", 18)
    val naturalGasAnnualDemandM3 = uinteger("natural_gas_annual_demand_m3").nullable()
    val percentageNaturalGasForHeating = uinteger("percentage_natural_gas_for_heating").nullable()

    /**
     * [com.zenmo.companysurvey.dto.Heat]
     */
    val heatingTypes = enumArray("heating_types", HeatingType::class)
    val sumGasBoilerKw = float("combined_gas_boiler_kw").nullable()
    val sumHeatPumpKw = float("combined_heat_pump_kw").nullable()
    val sumHybridHeatPumpElectricKw = float("combined_hybrid_heat_pump_electric_kw").nullable()
    val annualDistrictHeatingDemandGj = float("annual_district_heating_demand_gj").nullable()
    val localHeatExchangeDescription = varchar("local_heat_exchange_description", 1000)
    val hasUnusedResidualHeat = bool("has_unused_residual_heat").nullable()

    /**
     * [com.zenmo.companysurvey.dto.Storage]
     */
    val hasBattery = bool("has_battery").nullable()
    val batteryCapacityKwh = float("battery_capacity_kwh").nullable()
    val batteryPowerKw = float("battery_power_kw").nullable()
    val batterySchedule = varchar("battery_schedule", 1000)

    val hasPlannedBattery = bool("has_planned_battery").nullable()
    val plannedBatteryCapacityKwh = float("planned_battery_capacity_kwh").nullable()
    val plannedBatteryPowerKw = float("planned_battery_power_kw").nullable()
    val plannedBatterySchedule = varchar("planned_battery_schedule", 1000)

    val hasThermalStorage = bool("has_thermal_storage").nullable()
    val thermalStorageKw = float("thermal_storage_kw").nullable()

    /**
     * [com.zenmo.companysurvey.dto.Transport]
     */
    val hasVehicles = bool("has_vehicles").nullable()

    val numDailyCarAndVanCommuters = uinteger("num_daily_car_and_van_commuters").nullable()
    val numDailyCarVisitors = uinteger("num_daily_car_visitors").nullable()
    val numCommuterAndVisitorChargePoints = uinteger("num_commuter_and_visitor_charge_points").nullable()

    /**
     * [com.zenmo.companysurvey.dto.Trucks]
     */
    val numTrucks = uinteger("num_trucks").nullable()
    val numElectricTrucks = uinteger("num_electric_trucks").nullable()
    val numTruckChargePoints = uinteger("num_truck_charge_points").nullable()
    val powerPerTruckChargePointKw = float("power_per_truck_charge_point_kw").nullable()
    val annualTravelDistancePerTruckKm = uinteger("annual_travel_distance_per_truck_km").nullable()
    val numPlannedElectricTrucks = uinteger("num_planned_electric_trucks").nullable()
    val numPlannedHydgrogenTrucks = uinteger("num_planned_hydrogen_trucks").nullable()

    /**
     * [com.zenmo.companysurvey.dto.Vans]
     */
    val numVans = uinteger("num_vans").nullable()
    val numElectricVans = uinteger("num_electric_vans").nullable()
    val numVanChargePoints = uinteger("num_electric_van_charge_points").nullable()
    val powerPerVanChargePointKw = float("power_per_van_charge_point_kw").nullable()
    val annualTravelDistancePerVanKm = uinteger("annual_travel_distance_per_van_km").nullable()
    val numPlannedElectricVans = uinteger("num_planned_electric_vans").nullable()
    val numPlannedHydgrogenVans = uinteger("num_planned_hydrogen_vans").nullable()

    /**
     * [com.zenmo.companysurvey.dto.Cars]
     */
    val numCars = uinteger("num_cars").nullable()
    val numElectricCars = uinteger("num_electric_cars").nullable()
    val numCarChargePoints = uinteger("num_car_charge_points").nullable()
    val powerPerCarChargePointKw = float("power_per_car_charge_point_kw").nullable()
    val annualTravelDistancePerCarKm = uinteger("annual_travel_distance_per_car_km").nullable()
    val numPlannedElectricCars = uinteger("num_planned_electric_cars").nullable()
    val numPlannedHydgrogenCars = uinteger("num_planned_hydrogen_cars").nullable()

    /**
     * [com.zenmo.companysurvey.dto.OtherVehicles]
     */
    val hasOtherVehicles = bool("has_other_vehicles").nullable()
    val otherVehiclesDescription = varchar("other_vehicles_description", 5000)
}
