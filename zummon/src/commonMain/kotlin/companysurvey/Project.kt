package com.zenmo.zummon.companysurvey

import com.zenmo.zummon.KotlinUuidSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
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
//    @Contextual
    @OptIn(ExperimentalUuidApi::class)
    @Serializable(with = KotlinUuidSerializer::class)
    val id: Uuid? = null,
    val name: String = "",
    // Project ID aka Energy Hub ID of Energieke Regio.
    val energiekeRegioId: Int?,
    val buurtCodes: List<String> = emptyList(),
)
