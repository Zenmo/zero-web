package com.zenmo.ztor.minio


import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.InputStream
import java.util.*

object MinioExcelStorage {
    private val client = MinioClient.builder()
        .endpoint("https://minio.lux.energy")
        .credentials(
            System.getenv("MINIO_ACCESS_KEY"),
            System.getenv("MINIO_SECRET_KEY")
        )
        .build()

    private val bucket: String =
        System.getenv("MINIO_ER_EXCEL_UPLOAD_BUCKET") ?: "er-excel-uploads"

    private fun ensureBucketExists() {
        val exists = client.bucketExists(
            BucketExistsArgs.builder().bucket(bucket).build()
        )
        if (!exists) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build())
        }
    }

    fun saveExcel(
        inputStream: InputStream,
        size: Long,
        userId: UUID,
        fileName: String
    ): String {
        ensureBucketExists()

        val objectName = generateExcelObjectName(fileName)

        client.putObject(
            PutObjectArgs.builder()
                .bucket(bucket)
                .`object`(objectName)
                .stream(inputStream, size, -1)
                .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .userMetadata(
                    mapOf(
                        "user-id" to userId.toString(),
                        "original-filename" to fileName
                    )
                )
                .build()
        )
        return objectName
    }
}

private fun generateExcelObjectName(originalFilename: String): String {
    val now = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
    val safeName = originalFilename
        .replace("[^A-Za-z0-9._-]".toRegex(), "_")

    return buildString {
        append(now.year).append("/")
        append("%02d".format(now.monthNumber)).append("/")
        append("%02d".format(now.dayOfMonth)).append("/")
        append(UUID.randomUUID().toString().take(8)).append("-").append(safeName)
    }
}