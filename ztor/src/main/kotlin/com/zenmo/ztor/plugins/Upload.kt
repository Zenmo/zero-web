package com.zenmo.ztor.plugins

import com.zenmo.orm.blob.BlobPurpose
import com.zenmo.orm.companysurvey.FileRepository
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.zenmo.ztor.blob.BlobStorage
import com.zenmo.ztor.errorMessageToJson
import io.ktor.http.*
import org.jetbrains.exposed.sql.Database

fun Application.configureUpload(db: Database) {
    val blobStorage = BlobStorage()
    val fileRepository = FileRepository(db)

    routing {
        // Get a shared access signature (SAS) token that can be used to upload a blob.
        get("/upload-url") {
            val queryParams = call.request.queryParameters

            val required = listOf("project", "company", "fileName", "purpose")
            val missing = required.filter { !queryParams.contains(it) }
            if (missing.isNotEmpty()) {
                call.respond(HttpStatusCode.BadRequest, errorMessageToJson("Missing query parameters: ${missing.joinToString(", ")}"))
                return@get
            }

            val authorization = blobStorage.authorizeUpload(
                BlobPurpose.valueOf(queryParams["purpose"]!!),
                queryParams["project"]!!,
                queryParams["company"]!!,
                queryParams["fileName"]!!,
            )

            call.respond(authorization)
        }

        get("/download") {
            val queryParams = call.request.queryParameters

            val required = listOf("blobName")
            val missing = required.filter { !queryParams.contains(it) }
            if (missing.isNotEmpty()) {
                call.respond(HttpStatusCode.BadRequest, errorMessageToJson("Missing query parameters: ${missing.joinToString(", ")}"))
                return@get
            }

            val blobName = queryParams["blobName"]!!

            val blobClient = blobStorage.getBlobClient(blobName)
            if (!blobClient.exists()) {
                call.respond(HttpStatusCode.NotFound, errorMessageToJson("Blob not found at Azure"))
                return@get
            }

            val fileInfo = fileRepository.getFileByBlobName(blobName)
            if (fileInfo == null) {
                call.respond(HttpStatusCode.NotFound, errorMessageToJson("File info not found in database"))
                return@get
            }

            val contentType = fileInfo.contentType?.let { ContentType.parse(it) } ?: ContentType.Application.OctetStream

            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Inline.withParameter(ContentDisposition.Parameters.FileName, fileInfo.originalName).toString()
            )

            call.respondOutputStream(contentType, contentLength = fileInfo.size.toLong()) {
                blobClient.downloadStream(this)
            }
        }
    }
}