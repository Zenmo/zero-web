package com.zenmo.ztor.plugins

import com.zenmo.orm.blob.BlobPurpose
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.zenmo.ztor.blob.BlobStorage
import com.zenmo.ztor.errorMessageToJson
import io.ktor.http.*

fun Application.configureUpload() {
    val blobStorage = BlobStorage()

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
    }
}