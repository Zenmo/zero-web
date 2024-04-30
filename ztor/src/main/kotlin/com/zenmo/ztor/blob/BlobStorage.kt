package com.zenmo.ztor.blob

import com.azure.storage.blob.BlobServiceClientBuilder
import com.azure.storage.blob.sas.BlobSasPermission
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues
import com.azure.storage.common.sas.SasProtocol
import com.zenmo.orm.blob.BlobPurpose
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.streams.asSequence

class BlobStorage(
    azureAccountName: String = System.getenv("AZURE_STORAGE_ACCOUNT_NAME"),
    azureAccountKey: String = System.getenv("AZURE_STORAGE_ACCOUNT_KEY"),
    private val containerName: String = System.getenv("AZURE_STORAGE_CONTAINER"),
) {
    private val blobServiceClient = BlobServiceClientBuilder()
        .connectionString("DefaultEndpointsProtocol=https;AccountName=$azureAccountName;AccountKey=$azureAccountKey")
        .buildClient()

    /**
     * You can use the result of this function to directly upload a file to Azure Blob Storage.
     * This is similar to S3's pre-signed URLs.
     * You only need to set the `x-ms-blob-type` header to `BlockBlob`.
     */
    fun authorizeUpload(
        blobPurpose: BlobPurpose,
        project: String,
        company: String,
        fileName: String,
    ): UploadAuthorization {
        if (project === "") {
            throw Exception("Company name cannot be empty")
        }

        if (company === "") {
            throw Exception("Company name cannot be empty")
        }

        if (fileName === "") {
            throw Exception("File name cannot be empty")
        }

        val dateStamp = DateTimeFormatter
            .ofPattern("yyyy-MM-dd")
            .withZone(ZoneId.of("Europe/Amsterdam"))
            .format(Instant.now())

        val blobName = "${dateStamp}_${project}_${company}_${blobPurpose.toNamePart()}_${randomString(3u)}_${fileName}"

        val blobClient = blobServiceClient.getBlobContainerClient(containerName).getBlobClient(blobName)
        // We would like to (pre-)set some metadata but this is not (easily) possible.

        val permissions = BlobSasPermission().setReadPermission(true).setWritePermission(true).setCreatePermission(true)

        val expiryTime = OffsetDateTime.now().plusDays(1)

        val sasSignatureValues = BlobServiceSasSignatureValues(expiryTime, permissions)
            .setProtocol(SasProtocol.HTTPS_ONLY)

        val sasToken = blobClient.generateSas(sasSignatureValues)

        return UploadAuthorization(
            uploadUrl = "${blobClient.blobUrl}?$sasToken",
            blobName = blobName,
            originalName = fileName,
            sas = sasToken,
        )
    }

    fun getBlobClient(blobName: String) = blobServiceClient.getBlobContainerClient(containerName).getBlobClient(blobName)

    fun generateDownloadUrl(blobName: String): String {
        val blobClient = blobServiceClient.getBlobContainerClient(containerName).getBlobClient(blobName)

        val permissions = BlobSasPermission().setReadPermission(true)

        val expiryTime = OffsetDateTime.now().plusDays(1)

        val sasSignatureValues = BlobServiceSasSignatureValues(expiryTime, permissions)
            .setProtocol(SasProtocol.HTTPS_ONLY)

        val sasToken = blobClient.generateSas(sasSignatureValues)

        return "${blobClient.blobUrl}?$sasToken"
    }
}

fun randomString(length: UInt): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

    return java.util.Random().ints(length.toLong(), 0, chars.length)
        .asSequence()
        .map(chars::get)
        .joinToString("")
}