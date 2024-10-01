package com.zenmo.orm.companysurvey

import com.zenmo.orm.companysurvey.table.GridConnectionTable
import com.zenmo.orm.companysurvey.table.TimeSeriesTable
import org.jetbrains.exposed.sql.*
import com.zenmo.zummon.companysurvey.TimeSeries
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert
import java.util.UUID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class TimeSeriesRepository(
    val db: Database
) {
    fun insertByEan(ean: String, timeSeries: TimeSeries) {
        transaction(db) {
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
    }

    fun upsert(timeSeries: TimeSeries, gridConnectionId: UUID) {
        // TODO: this always does an INSERT and never UPDATE
        // because there is no unique constraint
        TimeSeriesTable.upsert(where = {
            (TimeSeriesTable.gridConnectionId eq gridConnectionId)
                .and(TimeSeriesTable.type eq timeSeries.type)
        }) {
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