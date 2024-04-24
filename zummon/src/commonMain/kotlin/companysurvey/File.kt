package com.zenmo.zummon.companysurvey

import kotlinx.serialization.Serializable

@Serializable
data class File (
    val blobName: String,
    val originalName: String,
    val contentType: String?,
    val size: Int,
)
