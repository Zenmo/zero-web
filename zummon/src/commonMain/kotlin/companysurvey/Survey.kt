package com.zenmo.zummon.companysurvey

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import kotlinx.serialization.Serializable
import com.zenmo.zummon.UuidSerializer
import kotlinx.datetime.*
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.format.char
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * Root object
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class Survey(
    @Serializable(with = UuidSerializer::class)
    val id: Uuid = uuid4(),
    val created: Instant = Clock.System.now().roundToMilliseconds(),
    val zenmoProject: String,
    val companyName: String,
    val personName: String,
    val email: String = "",
    val dataSharingAgreed: Boolean = false,

    val addresses: List<Address>,
) {
    /**
     * For JavaScript
     */
    public val addressArray: Array<Address>
        get() = addresses.toTypedArray()

    public val numGridConnections: Int
        get() = addresses.sumOf { it.gridConnections.size }

    public val createdToString: String
        get() = created.toString()

    public val filesArray: Array<File>
        get() = addresses.flatMap {
            it.gridConnections.flatMap {
                val result = it.electricity.quarterHourlyValuesFiles.toMutableList()
                result.addAll(it.naturalGas.hourlyValuesFiles)
                if (it.electricity.authorizationFile != null) {
                    result.add(it.electricity.authorizationFile)
                }
                return@flatMap result.toList()
            }
        }.toTypedArray()

    /**
     * Get the first grid connection.
     * Throws if there is not exactly one grid connection.
     */
    public fun getSingleGridConnection(): GridConnection {
        return addresses.firstAndOnly().gridConnections.firstAndOnly()
    }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun surveyFromJson(json: String): Survey {
    return kotlinx.serialization.json.Json.decodeFromString(Survey.serializer(), json)
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun surveysFromJson(json: String): Array<Survey> {
    return kotlinx.serialization.json.Json.decodeFromString<Array<Survey>>(json)
}

/**
 * Round to the precision that the database supports
 * so that the outgoing and incoming values are the same and can be compared in tests.
 */
fun Instant.roundToMilliseconds(): Instant {
    return Instant.fromEpochMilliseconds(this.toEpochMilliseconds())
}

private fun <T> List<T>.firstAndOnly(): T {
    if (size != 1) {
        throw IllegalArgumentException("Expected exactly one element, but got $size")
    }
    return this.first()
}