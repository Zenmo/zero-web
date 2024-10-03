package com.zenmo.orm.dbutil

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGInterval
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

/**
 * Map PostgreSQL Interval type to Kotlin Duration type.
 * This is only implemented in minutes for now.
 */
class IntervalColumnType : ColumnType<Duration>() {
    override fun sqlType(): String = "INTERVAL"

    override fun notNullValueToDB(value: Duration): PGInterval {
        val minutes = value.inWholeMinutes.toInt()
        return PGInterval(0, 0, 0, 0, minutes, 0.0)
    }

    private val regex = "^0 years 0 mons 0 days (?<hours>\\d+) hours (?<mins>\\d+) mins 0.0 secs$".toRegex()

    override fun valueFromDB(value: Any): Duration {
        if (value !is String) {
            return valueFromDB(value.toString())
        }

        val match =  regex.find(value) ?: throw Exception("Unrecognised postgres interval format: $value")

        val hours = match.groupValues[1].toInt().hours
        val minutes = match.groupValues[2].toInt().minutes

        return hours + minutes
    }

    override fun nonNullValueAsDefaultString(value: Duration): String =
        buildString {
            append("'")
            append(notNullValueToDB(value).toString())
            append("'::interval")
        }

//    override fun readObject(rs: ResultSet, index: Int): Any? {
//        // ResultSet.getLong returns 0 instead of null
//        return rs.getLong(index).takeIf { rs.getObject(index) != null }
//    }
}

fun Table.interval(name: String): Column<Duration> = registerColumn(name, IntervalColumnType())
