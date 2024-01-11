package com.zenmo

import com.zenmo.blob.BlobPurpose
import com.zenmo.companysurvey.dto.*
import com.zenmo.companysurvey.table.GridConnectionTable
import com.zenmo.companysurvey.table.CompanySurveyTable
import com.zenmo.companysurvey.table.FileTable
import com.zenmo.dbutil.createEnumTypeSql
import com.zenmo.energieprestatieonline.RawPandTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

val enums = listOf(
    KleinverbruikElectricityConnectionCapacity::class.java,
    KleinverbruikElectricityConsumptionProfile::class.java,
    HeatingType::class.java,
    PVOrientation::class.java,
    BlobPurpose::class.java,
    MissingPvReason::class.java,
)

val tables = arrayOf(
    CompanySurveyTable,
    GridConnectionTable,
    FileTable,
    RawPandTable,
)

fun createSchema(db: Database) {
    transaction(db) {
        execInBatch(schemaSql(db))
    }
}



// this is a utility function for development only
fun createMissingTablesAndColumns(db: Database) {
    transaction(db) {
        SchemaUtils.createMissingTablesAndColumns(*tables)
    }
}

fun schemaSql(db: Database): List<String> {
    val statements = mutableListOf<String>()

    enums.forEach {
        statements.add(createEnumTypeSql(it))
    }

    transaction(db) {
        statements.addAll(SchemaUtils.createStatements(*tables))
    }

    return statements
}

fun echoSchemaSql(db: Database) {
    val sql = schemaSql(db).reduce() { acc, s -> acc + ";\n" + s } + ";"
    println(sql)
}
