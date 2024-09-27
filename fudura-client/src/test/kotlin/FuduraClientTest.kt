package com.zenmo.fudura

import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertTrue

class FuduraClientTest {
    @Test
    fun testGetWalkTreeToLeaf() {
        val client = FuduraClient()
        val meteringPoints = client.getMeteringPoints()
        assertTrue(meteringPoints.eans.isNotEmpty())

        val meteringPointId = meteringPoints.eans.first().meteringPoints.first()

        val channels = client.getChannels(meteringPointId)
        assertTrue(channels.channels.isNotEmpty())

        val kwhChannels = channels.channels.map {
            Pair(meteringPointId, client.getChannelMetadata(meteringPointId, it))
        }.filter {
            it.second.unitOfMeasurement == UnitOfMeasurement.kWh
        }

        val channel = kwhChannels.first()

        val telemetry = client.getTelemetry(
            channel.first,
            channel.second.channelId,
            from = Instant.parse("2023-01-01T00:00:00+01:00"),
            to = Instant.parse(channel.second.lastReadingTimestamp),
        )
        assertTrue(telemetry.telemetry.size > 10)
    }
}
