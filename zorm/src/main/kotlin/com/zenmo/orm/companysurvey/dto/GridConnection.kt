package com.zenmo.orm.companysurvey.dto

import com.zenmo.orm.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class GridConnection(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),

    val electricity: Electricity = Electricity(),
    val supply: Supply = Supply(),
    val naturalGas: NaturalGas = NaturalGas(),
    val heat: Heat = Heat(),
    val storage: Storage = Storage(),
    val transport: Transport = Transport(),

    // open questions
    val energyOrBuildingManagementSystemSupplier: String = "",
    val mainConsumptionProcess: String = "",
    val consumptionFlexibility: String = "",
    val expansionPlans: String = "",
    val electrificationPlans: String = "",
    val surveyFeedback: String = "",
)
