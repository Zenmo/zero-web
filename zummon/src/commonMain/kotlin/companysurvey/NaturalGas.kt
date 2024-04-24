package com.zenmo.zummon.companysurvey

import kotlinx.serialization.Serializable

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
}
