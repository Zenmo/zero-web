package com.zenmo.models

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
    val surveyId = (uuid("survey_id") references CompanySurveyTable.id)
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
    val companyName: String,
    val personName: String,
    val email: String,
    val usageAssets: String,
    val generationAssets: String,
    val usagePattern: String,

    val electricityConnections: List<CompanyElectricityConnection>,
)

@Serializable
data class CompanyElectricityConnection (
    val street: String,
    val houseNumber: Int,
    val houseLetter: String,
    val houseNumberSuffix: String,
    val annualUsageKWh: Int?,
    val quarterlyValuesFile: String,
    val ean: String,
    val description: String,
)
