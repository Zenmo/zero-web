package com.zenmo.zummon.companysurvey

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import kotlinx.serialization.Serializable
import com.zenmo.zummon.UuidSerializer
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * Root object
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class Survey(
    @Serializable(with = UuidSerializer::class)
    val id: Uuid = uuid4(),
    val created: Instant = Clock.System.now(),
    val zenmoProject: String,
    val companyName: String,
    val personName: String,
    val email: String = "",
    val dataSharingAgreed: Boolean = false,

    val addresses: List<Address>,
)

@OptIn(ExperimentalJsExport::class)
@JsExport
fun surveyFromJson(json: String): Survey {
    return kotlinx.serialization.json.Json.decodeFromString(Survey.serializer(), json)
}
