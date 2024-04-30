package com.zenmo.zummon.companysurvey

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class NaturalGas (
    val hasConnection: Boolean? = null,
    val ean: String = "",
    val annualDemandM3: Int? = null,
    val hourlyValuesFiles: List<File> = emptyList(),
    val hourlyUsage: List<HourlyGasUsage> = emptyList(),
    val percentageUsedForHeating: Int? = null,
) {
    fun getHasConnection(): Boolean {
        return hasConnection ?: false
    }

    /**
     * For JavaScript
     */
    val hourlyValuesFileArray: Array<File>
        get() = hourlyValuesFiles.toTypedArray()
}
