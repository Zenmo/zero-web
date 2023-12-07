package com.zenmo.companysurvey.dto

import com.zenmo.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class GridConnection(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),

    val electricity: Electricity = Electricity(),
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
    val surveyFeedback: String,
)
