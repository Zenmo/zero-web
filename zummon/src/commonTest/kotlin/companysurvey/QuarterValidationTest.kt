package companysurvey

import com.zenmo.zummon.companysurvey.Electricity
import com.zenmo.zummon.companysurvey.NaturalGasValidator
import com.zenmo.zummon.companysurvey.Status
import com.zenmo.zummon.companysurvey.TimeSeries
import com.zenmo.zummon.companysurvey.TimeSeriesType
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class QuarterValidationTest {
    val naturalGasValidator = NaturalGasValidator()

    @Test
    fun `validateQuarterHourlyDelivery with no data provided`() {
        val electricity = Electricity(
            quarterHourlyDelivery_kWh = null
        )
        val result = naturalGasValidator.validateQuarterHourlyDeliveryData(electricity)
        assertEquals(Status.MISSING_DATA, result.status)
        assertEquals("Quarter-hourly delivery data is not provided", result.message)
    }

    @Test
    fun `validateQuarterHourlyDeliveryData with valid data and no holes`() {
        val electricity = Electricity(
            quarterHourlyDelivery_kWh = TimeSeries(
                type = TimeSeriesType.ELECTRICITY_DELIVERY,
                start = Instant.parse("2022-01-01T00:00:00Z"),
                values = listOf(1.2f, 2.2f, 3.2f, 4.2f)
            )
        )
        val result = naturalGasValidator.validateQuarterHourlyDeliveryData(electricity)
        assertEquals(Status.VALID, result.status)
        assertEquals("Quarter-hourly delivery data has no gaps exceeding the limit", result.message)
    }

    @Test
    fun `validateQuarterHourlyDeliveryData with valid data and small gaps within limit`() {
        val electricity = Electricity(
            quarterHourlyDelivery_kWh = TimeSeries(
                type = TimeSeriesType.ELECTRICITY_DELIVERY,
                start = Instant.parse("2022-01-01T00:00:00Z"),
                values = listOf(1.2f, null, null, 2.2f, null, 3.2f, 4.2f) // Only 2 nulls in a row
            )
        )
        val result = naturalGasValidator.validateQuarterHourlyDeliveryData(electricity)
        assertEquals(Status.VALID, result.status)
        assertEquals("Quarter-hourly delivery data has no gaps exceeding the limit", result.message)
    }

    @Test
    fun `validateQuarterHourlyDeliveryData with invalid data having gaps exceeding 4 days`() {
        val electricity = Electricity(
            quarterHourlyDelivery_kWh = TimeSeries(
                type = TimeSeriesType.ELECTRICITY_DELIVERY,
                start = Instant.parse("2022-01-01T00:00:00Z"),
                values = List(385) { null } + listOf(1.2f, 2.2f) // 385 nulls, exceeding limit
            )
        )
        val result = naturalGasValidator.validateQuarterHourlyDeliveryData(electricity)
        assertEquals(Status.INVALID, result.status)
        assertEquals("Quarter-hourly delivery data has a gap of 96 hours, exceeding the allowed limit", result.message)
    }

    @Test
    fun `validateQuarterHourlyDeliveryData with exact gap limit`() {
        val electricity = Electricity(
            quarterHourlyDelivery_kWh = TimeSeries(
                type = TimeSeriesType.ELECTRICITY_DELIVERY,
                start = Instant.parse("2022-01-01T00:00:00Z"),
                values = List(384) { null } + listOf(1.2f, 2.2f) // 384 nulls, at the limit
            )
        )
        val result = naturalGasValidator.validateQuarterHourlyDeliveryData(electricity)
        assertEquals(Status.VALID, result.status)
        assertEquals("Quarter-hourly delivery data has no gaps exceeding the limit", result.message)
    }
}
