package com.zenmo.zummon.companysurvey

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import com.zenmo.zummon.BenasherUuidSerializer
import com.zenmo.zummon.addNotNull
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class GridConnection(
    @Serializable(with = BenasherUuidSerializer::class)
    val id: Uuid = uuid4(),

    // Is always set when object comes from the database.
    val sequence: Int? = null,

    val electricity: Electricity = Electricity(),
    val supply: Supply = Supply(),
    val naturalGas: NaturalGas = NaturalGas(),
    val heat: Heat = Heat(),
    val storage: Storage = Storage(),
    val transport: Transport = Transport(),
    val pandIds: Set<PandID> = emptySet(),

    // open questions
    val energyOrBuildingManagementSystemSupplier: String = "",
    val mainConsumptionProcess: String = "",
    val consumptionFlexibility: String = "",
    val expansionPlans: String = "",
    val electrificationPlans: String = "",
    val surveyFeedback: String = "",
) {
    fun clearId() = copy(id = uuid4())

    fun allTimeSeries(): List<TimeSeries> {
        val list = electricity.allTimeSeries().toMutableList()
        list.addAll(heat.allTimeSeries())
        list.addNotNull(naturalGas.hourlyDelivery_m3)
        list.addNotNull(transport.agriculture.dieselUsageTimeSeries)
        return list.toList()
    }
}
