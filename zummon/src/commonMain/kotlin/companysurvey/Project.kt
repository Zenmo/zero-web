package com.zenmo.zummon.companysurvey

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalJsExport::class)
@Serializable
@JsExport
data class Project
@OptIn(ExperimentalUuidApi::class)
constructor(
    @Contextual
    val id: Uuid,
    val name: String,
    // Project ID aka Energy Hub ID of Energieke Regio.
    val energiekeRegioId: Int?,
    val buurtCodes: List<String>,
)
