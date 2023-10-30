package com.zenmo.companysurvey.dto

import kotlinx.serialization.Serializable

@Serializable
data class NaturalGas (
    val hasConnection: Boolean?,
    val ean: String,
    val annualDemandM3: Int?,
    val hourlyValuesFile: List<File>,
    val percentageUsedForHeating: Int?,
)
