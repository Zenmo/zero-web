package com.zenmo.zummon.companysurvey

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

val pandIdRegex = Regex("^\\d{16}\$")

/**
 * Would prefer it if it was an inline value class but JS export does not support it
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class PandID(val value: String) {
    init {
        if (!value.matches(pandIdRegex)) {
            throw IllegalArgumentException("Pand ID should have 16 digits. Got: $value")
        }
    }
}
