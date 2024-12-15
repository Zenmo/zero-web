package com.zenmo.zummon.companysurvey

import com.zenmo.zummon.BenasherUuidSerializer
import kotlinx.serialization.Serializable
import kotlin.js.JsExport
import kotlin.uuid.Uuid

@Serializable
@JsExport
data class Project
constructor(
//    @Contextual
    @Serializable(with = BenasherUuidSerializer::class)
    val id: Uuid,
    val name: String = "",
    // Project ID aka Energy Hub ID of Energieke Regio.
    val energiekeRegioId: Int?,
    val buurtCodes: List<String> = emptyList(),
)

@JsExport
fun projectsFromJson(json: String): Array<Project> {
    return kotlinx.serialization.json.Json.decodeFromString<Array<Project>>(json)
}
