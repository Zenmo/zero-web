package com.zenmo.companysurvey.table

import com.zenmo.blob.BlobPurpose
import com.zenmo.dbutil.PGEnum
import org.jetbrains.exposed.sql.Table

object FileTable: Table("file") {
    val gridConnectionId = uuid("grid_connection_id").references(GridConnectionTable.id)
    val purpose = customEnumeration(
        "purpose",
        BlobPurpose::class.simpleName,
        fromDb = { BlobPurpose.valueOf(it as String) },
        toDb = { PGEnum(BlobPurpose::class.simpleName!!, it) })

    val blobName = varchar("remote_name", 1000)
    val originalName = varchar("original_name", 100)
    val contentType = varchar("content_type", 100).nullable()
    val size = integer("size")

    override val primaryKey = PrimaryKey(blobName)
}
