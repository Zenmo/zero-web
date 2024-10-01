package com.zenmo.ztor

import com.zenmo.fudura.DetailedMeteringPoint
import com.zenmo.fudura.Direction
import com.zenmo.orm.companysurvey.SurveyRepository
import com.zenmo.fudura.FuduraClient
import com.zenmo.fudura.GetChannelMetadataResult
import com.zenmo.fudura.Telemetry
import com.zenmo.fudura.UnitOfMeasurement
import com.zenmo.orm.companysurvey.TimeSeriesRepository
import com.zenmo.zummon.companysurvey.Survey
import com.zenmo.zummon.companysurvey.TimeSeries
import com.zenmo.zummon.companysurvey.TimeSeriesType
import com.zenmo.zummon.companysurvey.TimeSeriesUnit
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

class FuduraImport (
    val surveyRepository: SurveyRepository,
    val timeSeriesRepository: TimeSeriesRepository,
    val fuduraClient: FuduraClient = FuduraClient(),
) {
    fun importHessenPoort() {
        val detailsMeteringPoints = fuduraClient.getDetailedMeteringPoints(perCustomer = true, customerId = "*")
        val eanMeteringPoints = detailsMeteringPoints.meteringPoints.filter {
            // ignore extra meters for now
            it.ean == it.meteringPointId
        }
        val inRangeEanMeteringPoints = eanMeteringPoints.filter {
            val period = it.authorizations.single().periods.single()
            if (period.from > "2022-12-31") {
                return@filter false
            }

            if (period.to < "2024-01-01") {
                return@filter false
            }

            return@filter true
        }

        val surveys = surveyRepository.getHessenpoortSurveys()

        val perCustomerData: Collection<FuduraCustomer> = inRangeEanMeteringPoints.groupingBy { it.authorizations.single().customerId }
            .aggregate { key, accumulator: FuduraCustomer?, element, first ->
                val companyName = findCompanyNameByEan(surveys, element.ean)
                if (companyName != null) {
                    println("No survey found for ean ${element.ean}, Fudura customer ${key}")
                }
                if (first) {
                    FuduraCustomer(key, companyName, listOf(element))
                } else {
                    accumulator!!.copy(
                        eans = accumulator.eans,
                        companyName = if (companyName == accumulator.companyName) {
                            companyName
                        } else {
                            "${accumulator.companyName}+${companyName}"
                        }
                    )
                }
            }.values

        val perKnownCustomerData = perCustomerData.filter {
            if (it.companyName == null) {
                println("No survey found for all EANs of Fudura customer ${it.id}, don't know which company this is")
            }

            it.companyName != null
        }

        for (customer in perKnownCustomerData) {
            for (eanDetails in customer.eans) {
                val channels = fuduraClient.getChannels(eanDetails.meteringPointId).channels
                val channelMetadataCollection = channels.map { channel ->
                    fuduraClient.getChannelMetadata(eanDetails.meteringPointId, channel)
                }.filter { isChannelRelevant(it) }

                if (channelMetadataCollection.isEmpty()) {
                    println("No relevant channels for ean ${eanDetails.ean}, company ${customer.companyName}")
                }

                for (channel in channelMetadataCollection) {
                    val telemetry = fuduraClient.getTelemetryRecursive(
                        eanDetails.meteringPointId,
                        channel.channelId,
                        Instant.parse("2023-01-01T00:14:59+01:00"),
                        Instant.parse("2024-01-01T00:00:00+01:00"),
                    )

                    if (telemetry.size != 35040) {
                        throw Exception("Not enough values")
                    }

                    val timeSeries = createTimeSeries(channel, telemetry)
                    timeSeriesRepository.insertByEan(eanDetails.ean, timeSeries)
                }
            }
        }
    }
}

data class FuduraCustomer (
    val id: String,
    val companyName: String?,
    val eans: List<DetailedMeteringPoint>,
)

fun findCompanyNameByEan(surveys: List<Survey>, ean: String): String? {
    val survey = surveys.find {  survey ->
        for (gc in survey.flattenedGridConnections()) {
            if (gc.electricity.ean == ean || gc.naturalGas.ean == ean) {
                return@find true
            }
        }

        false
    }

    return survey?.companyName
}

fun isChannelRelevant(channelMetadata: GetChannelMetadataResult) =
    channelMetadata.unitOfMeasurement == UnitOfMeasurement.kWh
            && channelMetadata.interval == "00:15:00"
            && channelMetadata.firstReadingTimestamp < "2022-12"
            && channelMetadata.lastReadingTimestamp > "2024-01"
            && channelMetadata.direction in listOf(Direction.Production, Direction.Consumption) // ignore net measurement

fun createTimeSeries(
    channelMetadata: GetChannelMetadataResult,
    telemetry: List<Telemetry>,
) = TimeSeries(
    type = when (channelMetadata.direction) {
        Direction.Production -> TimeSeriesType.ELECTRICITY_FEED_IN
        Direction.Consumption -> TimeSeriesType.ELECTRICITY_DELIVERY
        else -> throw Exception("unknown")
    },
    // Measurement start time
    start = Instant.parse(telemetry.first().readingTimestamp),
    timeStep = 15.minutes,
    unit = TimeSeriesUnit.KWH,
    values = telemetry.map {
        it.value.toFloat()
    }.toFloatArray()
)

