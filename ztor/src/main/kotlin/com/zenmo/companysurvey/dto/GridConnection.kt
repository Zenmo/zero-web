package com.zenmo.companysurvey.dto

import kotlinx.serialization.Serializable

@Serializable
data class GridConnection(
    val electricity: Electricity,
    val supply: Supply,
    val naturalGas: NaturalGas,
    val heat: Heat,
    val storage: Storage,
    val transport: Transport,

    // open questions
    val energyOrBuildingManagementSystemSupplier: String,
    val mainConsumptionProcess: String,
    val consumptionFlexibility: String,
    val electrificationPlans: String,
)
