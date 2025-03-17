package com.zenmo.joshi

import kotlinx.serialization.Serializable
import kotlin.js.JsExport
import kotlin.uuid.Uuid

/**
 * A variant of the company survey which has less properties
 * so it can be used for a list on a web page.
 */
@JsExport
@Serializable
data class IndexSurvey(
    val id: Uuid,
    val companyName: String,
)
