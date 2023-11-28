package com.zenmo.companysurvey.dto

import kotlinx.serialization.Serializable

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
    val consumptionFlexibility: String,
    val electrificationPlans: String,
)