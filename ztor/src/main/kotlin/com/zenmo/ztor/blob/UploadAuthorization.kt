package com.zenmo.ztor.blob

import kotlinx.serialization.Serializable

@Serializable
data class UploadAuthorization(
    /**
     * [uploadUrl] includes [sas]
     */
    val uploadUrl: String,
    val blobName: String,
    val originalName: String,
    /**
     * Shared Access Signature (SAS) token
     */
    val sas: String,
)
