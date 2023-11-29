package com.zenmo.companysurvey.dto

import kotlinx.serialization.Serializable

@Serializable
data class NaturalGas (
    val hasConnection: Boolean? = null,
    val ean: String = "",
    val annualDemandM3: Int? = null,
    val hourlyValuesFile: List<File> = emptyList(),
    val percentageUsedForHeating: Int? = null,
)
