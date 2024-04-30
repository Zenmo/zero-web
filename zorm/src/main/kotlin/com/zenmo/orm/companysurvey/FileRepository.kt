package com.zenmo.orm.companysurvey

import com.zenmo.orm.companysurvey.table.FileTable
import com.zenmo.zummon.companysurvey.File
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class FileRepository(
    private val db: Database
) {
    fun getFileByBlobName(blobName: String): File? = transaction(db) {
        FileTable.selectAll().where { FileTable.blobName eq blobName }.firstOrNull()?.let {
            File(
                blobName = it[FileTable.blobName],
                originalName = it[FileTable.originalName],
                contentType = it[FileTable.contentType],
                size = it[FileTable.size]
            )
        }
    }
}