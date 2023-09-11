package com.zenmo.models.companysurvey

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

object CompanySurveyTable: Table("company_survey") {
    val id = uuid("id").autoGenerate()
    val companyName = varchar("companyName", 50)
    val personName = varchar("personName", 50)
    val email = varchar("email", 50)
    val usageAssets = varchar("usageAssets", 10_000)
    val generationAssets = varchar("generationAssets", 10_000)
    val usagePattern = varchar("usagePattern", 10_000)

    override val primaryKey = PrimaryKey(id)
}

object CompanySurveyElectricityConnectionTable: Table("company_survey_electricity_connection") {
    val id = uuid("id").autoGenerate()
    val surveyId = uuid("survey_id").references(CompanySurveyTable.id)
    val street = varchar("street", 50)
    val houseNumber = integer("house_number")
    val houseLetter = varchar("house_letter", 1)
    val houseNumberSuffix = varchar("house_number_addition", 50)
    val annualUsageKWh = integer("annual_usage_kwh").nullable()
    val quarterlyValuesFile = varchar("quarterly_values_file", 100)
    val ean = varchar("ean", 18)
    val description = varchar("description", 1000)

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class CompanySurvey(
    val created: Instant,
    val companyName: String,
    val personName: String,
    val email: String,

    val transport: CompanyTransport,
    val electricityConnections: List<CompanyElectricityConnection>,
)

@Serializable
data class CompanyElectricityConnection (
    val street: String,
    val houseNumber: Int,
    val houseLetter: String,
    val houseNumberSuffix: String,
    val city: String,

    val ean: String?,
    val remarks: String,

    val quarterlyValuesFiles: List<File>,
    val hasGeneration: Boolean?,
    val annualElectricityDemand: Int?,
    val annualElectricityProduction: Int?,
    val electricityConsumptionProfile: ElectricityConsumptionProfile?,

    val kleinverbruik: CompanyKleinverbruik?,
    val grootverbruik: CompanyGrootverbruik?,
    val generation: Generation?,
    val remark: String?,
)

@Serializable
data class CompanyKleinverbruik (
    val connectionCapacity: ConnectionCapacity?,
    val consumptionProfile: ElectricityConsumptionProfile?,
)

@Serializable
data class CompanyGrootverbruik (
    val contractedConnectionDemandCapacityKw: Int?,
    val contractedConnectionSupplyCapacityKw: Int?,
)

@Serializable
data class Generation (
    val pvInstalledKwp: Int?,
    val pvOrientation: PVOrientation?,

    val pvPlanned: Boolean?,
    val pvPlannedCapacityKwp: Int?,
    val pvPlannedYear: Int?,

    val windInstalledKw: Double?,
    val otherSupply: String?,
)

enum class PVOrientation {
    SOUTH,
    EASTWEST,
    OTHER,
    UNKNOWN,
}

enum class ConnectionCapacity {
    // The total number of kleinverbruik postal code regions is 359.299.
    // This statistic includes residential connections.
    `1x40A`, // majority of connections in 6.491 postalcodes
    `1x50A`, // majority of connections in 2.226 postalcodes
    `3x25A`, // majority of connections in 125.261 postalcodes
    `3x35A`, // majority of connections in 1.049 postalcodes
    `3x50A`, // majority of connections in 180 postalcodes
    `3x63A`, // majority of connections in 261 postalcodes
    `3x80A`, // majority of connections in 613 postalcodes
}

// TODO: create profile names
enum class ElectricityConsumptionProfile {
    ONE,
    TWO,
    THREE,
    FOUR,
}

