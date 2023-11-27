package com.zenmo.companysurvey.table

import com.zenmo.companysurvey.dto.KleinverbruikElectricityConnectionCapacity
import com.zenmo.companysurvey.dto.KleinverbruikElectricityConsumptionProfile
import com.zenmo.companysurvey.dto.HeatingType
import com.zenmo.companysurvey.dto.PVOrientation
import com.zenmo.dbutil.PGEnum
import com.zenmo.dbutil.enumArray
import org.jetbrains.exposed.sql.Table

object CompanySurveyGridConnectionTable: Table("company_survey_grid_connection") {
    val id = uuid("id").autoGenerate()
    val surveyId = uuid("survey_id").references(CompanySurveyTable.id)

    /**
     * [com.zenmo.companysurvey.dto.Address]
     */
    val street = varchar("street", 50)
    val houseNumber = uinteger("house_number")
    val houseLetter = varchar("house_letter", 1)
    val houseNumberSuffix = varchar("house_number_addition", 50)
    val postalCode = varchar("postal_code", 8)
    val city = varchar("city", 50)

    /**
     * [com.zenmo.companysurvey.dto.Electricity]
     */
    val electricityEan = varchar("electricity_ean", 18)
    val quarterHourlyElectricityObjectKey = varchar("quarter_hourly_electricity_object_key", 100) // is this sufficient?
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
    val pvPlannedCapacityKwp = uinteger("pv_planned_capacity_kwp").nullable()
    val pvPlannedYear = uinteger("pv_planned_year").nullable()
    val windInstalledKw = float("wind_installed_kw").nullable()
    val otherSupply = varchar("other_supply", 1000)

    /**
     * [com.zenmo.companysurvey.dto.NaturalGas]
     */
    val hasNaturalGasConnection = bool("has_natural_gas_connection").nullable()
    val naturalGasEan = varchar("natural_gas_ean", 18)
    val naturalGasAnnualDemandM3 = uinteger("natural_gas_annual_demand_m3").nullable()
    val hourlyNaturalGasObjectKey = varchar("hourly_natural_gas_object_key", 100)
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

    override val primaryKey = PrimaryKey(id)
}
