package com.zenmo.companysurvey.dto

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Root object
 */
@Serializable
data class Survey(
    val created: Instant = Clock.System.now(),
    val companyName: String,
    val personName: String,
    val email: String,

    val transport: Transport,
    val gridConnections: List<SurveyGridConnection>,
)

@Serializable
data class SurveyGridConnection(
    val address: Address,

    val electricity: Electricity,
    val supply: Supply,
    val naturalGas: NaturalGas,
    val heat: Heat,
    val storage: Storage,

    // open questions
    val mainConsumptionProcess: String,
    val electrificationPlans: String,
)

@Serializable
data class Address(
    val street: String,
    val houseNumber: Int,
    val houseLetter: String, // A-Z allowed
    val houseNumberSuffix: String,
    val postalCode: String,
    val city: String,
)
