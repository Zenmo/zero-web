package com.zenmo.zummon.companysurvey

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import kotlinx.serialization.Serializable
import com.zenmo.zummon.BenasherUuidSerializer
import com.zenmo.zummon.User
import kotlinx.datetime.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.js.JsExport
import kotlinx.serialization.json.Json

/**
 * Root object
 */
@JsExport
@Serializable
data class Survey(
    @Serializable(with = BenasherUuidSerializer::class)
    val id: Uuid = uuid4(),
    val createdAt: Instant = Clock.System.now().roundToMilliseconds(),
    val zenmoProject: String,
    val companyName: String,
    val personName: String,
    val email: String = "",
    val dataSharingAgreed: Boolean = false,
    val addresses: List<Address>,
    val project: Project? = null,
    val createdBy: User? = null,
    val includeInSimulation: Boolean = true,
) {
    /**
     * For JavaScript
     */
    public val addressArray: Array<Address>
        get() = addresses.toTypedArray()

    public val numGridConnections: Int
        get() = addresses.sumOf { it.gridConnections.size }

    public fun flattenedGridConnections(): Iterable<GridConnection> = Iterable {
        iterator {
            for (address in addresses) {
                yieldAll(address.gridConnections)
            }
        }
    }

    /**
     * For sorting in JavaScript primereact/datatable
     */
    public val createdAtToString: String
        get() = createdAt.toString()

    /**
     * For sorting in JavaScript primereact/datatable
     */
    public val createdByToString: String
        get() = createdBy?.note ?: ""

    public val filesArray: Array<File>
        get() = addresses.flatMap {
            it.gridConnections.flatMap {
                val result = it.electricity?.quarterHourlyValuesFiles?.toMutableList() ?: mutableListOf()
                result.addAll(it.naturalGas.hourlyValuesFiles)
                if (it.electricity?.authorizationFile != null) {
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

    /**
     * Adds a Pand ID when it's not present or removes it if it is.
     * Only works for surveys with exactly one grid connection.
     */
    public fun togglePandId(pandId: PandID): Survey {
        val address = addresses.firstAndOnly()
        val gridConnection = address.gridConnections.firstAndOnly()
        var pandIds = gridConnection.pandIds

        if (pandIds.contains(pandId)) {
            pandIds = gridConnection.pandIds - pandId
        } else {
            pandIds = gridConnection.pandIds + pandId
        }

        return this.copy(
            addresses = listOf(
                address.copy(
                    gridConnections = listOf(
                        gridConnection.copy(
                            pandIds = gridConnection.pandIds + pandId
                        )
                    )
                )
            )
        )
    }

    /**
     * Add Pand ID to survey with exactly one grid connection.
     */
    public fun withPandId(pandId: PandID): Survey {
        val address = addresses.firstAndOnly()
        val gridConnection = address.gridConnections.firstAndOnly()

        return this.copy(
            addresses = listOf(
                address.copy(
                    gridConnections = listOf(
                        gridConnection.copy(
                            pandIds = gridConnection.pandIds + pandId
                        )
                    )
                )
            )
        )
    }

    /**
     * Remove Pand ID from survey with exactly one grid connection.
     */
    public fun withoutPandId(pandId: PandID): Survey {
        val address = addresses.firstAndOnly()
        val gridConnection = address.gridConnections.firstAndOnly()

        return this.copy(
            addresses = listOf(
                address.copy(
                    gridConnections = listOf(
                        gridConnection.copy(
                            pandIds = gridConnection.pandIds - pandId
                        )
                    )
                )
            )
        )
    }

    fun allPandIds(): Set<PandID> = this.flattenedGridConnections().flatMap { it.pandIds }.toSet()

    public fun withIncludeInSimulation(includeInSimulation: Boolean): Survey {
        return this.copy(includeInSimulation = includeInSimulation)
    }

    @OptIn(ExperimentalSerializationApi::class)
    public fun toPrettyJson(): String {
        val prettyJson = Json { // this returns the JsonBuilder
            prettyPrint = true
            // optional: specify indent
            prettyPrintIndent = "    "
        }
        return prettyJson.encodeToString(Survey.serializer(), this)
    }
}

@JsExport
fun surveyFromJson(json: String): Survey {
    return kotlinx.serialization.json.Json.decodeFromString(Survey.serializer(), json)
}

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
