package com.zenmo.models.companysurvey

import kotlinx.serialization.Serializable

@Serializable
data class File (
    val remoteName: String,
    val originalName: String,
    val contentType: String?,
    val size: Int,
)
