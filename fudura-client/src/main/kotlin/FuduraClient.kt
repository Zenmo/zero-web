package com.zenmo.fudura

import org.http4k.client.JavaHttpClient
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.then
import org.http4k.format.KotlinxSerialization.json
import kotlinx.datetime.Instant
import org.http4k.core.Uri
import org.http4k.filter.ClientFilters

/**
 * Client to get metering data.
 * Documentation is at https://developers.fudura.nl/api-details#api=telemetry-api
 */
class FuduraClient(
    baseUrl: String = "https://api.fudura.nl/telemetry",
    secretKey: String = requireNotNull(System.getenv("FUDURA_API_SECRET")) {
        "Please set the FUDURA_API_SECRET environment variable"
    }
) {
    private val httpHandler: HttpHandler = authenticationFilter(secretKey)
        .then(ClientFilters.SetBaseUriFrom(Uri.of(baseUrl)))
        .then(JavaHttpClient())

    fun getMeteringPoints(customerId: String = "*"): GetMeteringPointsResult {
        val request = Request(Method.GET, "/meteringpoints")
            .query("customerId", customerId)
        val response = httpHandler(request)
        checkStatusCode(request, response)
        return response.json()
    }

    fun getChannels(meteringPointId: String, customerId: String = "*"): GetChannelsResult {
        val request = Request(Method.GET, "/meteringpoints/$meteringPointId/channels")
            .query("customerId", customerId)
        val response = httpHandler(request)
        checkStatusCode(request, response)
        return response.json()
    }

    fun getChannelMetadata(
        meteringPointId: String, channelId: String, customerId: String = "*"
    ): GetChannelMetadataResult {
        val request = Request(Method.GET, "/meteringpoints/$meteringPointId/channels/$channelId")
            .query("customerId", customerId)
        val response = httpHandler(request)
        checkStatusCode(request, response)
        return response.json()
    }

    fun getTelemetry(
        meteringPointId: String,
        channelId: String,
        from: Instant? = null,
        to: Instant? = null,
        continutationToken: String? = null,
        customerId: String = "*",
    ): GetTelemetryResult {
        var request = Request(Method.GET, "/meteringpoints/$meteringPointId/channels/$channelId/query")
            .letIfNotNull(from) { it.query("from", from.toString()) }
            .letIfNotNull(to) { it.query("to", to.toString()) }
            .letIfNotNull(continutationToken) { it.query("continuationToken", continutationToken) }
            .letIfNotNull(customerId) { it.query("customerId", customerId) }

        val response = httpHandler(request)
        checkStatusCode(request, response)
        return response.json()
    }
}

private fun authenticationFilter(secretKey: String) = Filter { next -> {
    next(it.header("Ocp-Apim-Subscription-Key", secretKey))
}}

private inline fun <T, R> R.letIfNotNull(variable: T, block: (R) -> R): R =
    if (variable == null) {
        this
    } else {
        block(this)
    }

private fun checkStatusCode(request: Request, response: Response) {
    if (!response.status.successful) {
        throw RuntimeException("Error ${response.status.code} from ${request.method} ${request.uri}")
    }
}
