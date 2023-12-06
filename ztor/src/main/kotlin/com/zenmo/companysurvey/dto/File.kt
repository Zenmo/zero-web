package com.zenmo.companysurvey.dto

import kotlinx.serialization.Serializable

@Serializable
data class File (
    val blobName: String,
    val originalName: String,
    val contentType: String?,
    val size: Int,
)
