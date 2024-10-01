package com.zenmo.orm.companysurvey

import com.zenmo.orm.companysurvey.table.GridConnectionTable
import com.zenmo.orm.companysurvey.table.TimeSeriesTable
import com.zenmo.zummon.companysurvey.TimeSeries
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert
import java.util.UUID

class TimeSeriesRepository(
    val db: Database
) {
    fun insertByEan(ean: String, timeSeries: TimeSeries) {
        val gcId = GridConnectionTable
            .select(listOf(GridConnectionTable.id))
            .where {
                GridConnectionTable.electricityEan eq ean
            }
            .single()[GridConnectionTable.id]

        if (gcId == null) {
            throw RuntimeException("No gridconnection found with ean $ean")
        }

        transaction(db) {
            upsert(timeSeries, gcId)
        }
    }

    fun upsert(timeSeries: TimeSeries, gridConnectionId: UUID) {
        TimeSeriesTable.upsert {
            it[id] = timeSeries.id
            it[TimeSeriesTable.gridConnectionId] = gridConnectionId
            it[type] = timeSeries.type
            it[start] = timeSeries.start
            it[timeStep] = timeSeries.timeStep
            it[unit] = timeSeries.unit
            it[values] = timeSeries.values.toList()
        }
    }
}