package companysurvey

import com.zenmo.zummon.companysurvey.*
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class QuarterValidationTest {
    val electricityValidator = ElectricityValidator()

    @Test
    fun validateQuarterHourlyDeliveryNoProvide() {
        val electricity = Electricity(
            quarterHourlyDelivery_kWh = null
        )
        val result = electricityValidator.validateQuarterHourlyDeliveryData(electricity)
        assertEquals(Status.MISSING_DATA, result.status)
        assertEquals("Quarter-hourly delivery data is not provided", result.message)
    }

    @Test
    fun validateQuarterHourlyDeliveryDataValidNoHoles() {
        val electricity = Electricity(
            quarterHourlyDelivery_kWh = TimeSeries(
                type = TimeSeriesType.ELECTRICITY_DELIVERY,
                start = Instant.parse("2022-01-01T00:00:00Z"),
                values = floatArrayOf(1.2f, 2.2f, 3.2f, 4.2f)
            )
        )
        val result = electricityValidator.validateQuarterHourlyDeliveryData(electricity)
        assertEquals(Status.VALID, result.status)
        assertEquals("Quarter-hourly delivery data has no gaps exceeding the limit", result.message)
    }

    @Test
    fun validateQuarterHourlyDeliveryDataSmallGaps() {
        val electricity = Electricity(
            quarterHourlyDelivery_kWh = TimeSeries(
                type = TimeSeriesType.ELECTRICITY_DELIVERY,
                start = Instant.parse("2022-01-01T00:00:00Z"),
                values = floatArrayOf(1.2f, 0.0f, 0.0f, 2.2f, 0.0f, 3.2f, 4.2f) // Only 2 nulls in a row
            )
        )
        val result = electricityValidator.validateQuarterHourlyDeliveryData(electricity)
        assertEquals(Status.VALID, result.status)
        assertEquals("Quarter-hourly delivery data has no gaps exceeding the limit", result.message)
    }

    @Test
    fun validateQuarterHourlyDeliveryDataGapsExceed() {
        val electricity = Electricity(
            quarterHourlyDelivery_kWh = TimeSeries(
                type = TimeSeriesType.ELECTRICITY_DELIVERY,
                start = Instant.parse("2022-01-01T00:00:00Z"),
                values = FloatArray(385) { 0.0f } + floatArrayOf(1.2f, 2.2f) // 385 nulls, exceeding limit
            )
        )
        val result = electricityValidator.validateQuarterHourlyDeliveryData(electricity)
        assertEquals(Status.INVALID, result.status)
        assertEquals("Quarter-hourly delivery data has a gap of 96 hours, exceeding the allowed limit", result.message)
    }

    @Test
    fun validateQuarterHourlyProductionDataInvalidGaps() {
        val electricity = Electricity(
            quarterHourlyProduction_kWh = TimeSeries(
                type = TimeSeriesType.ELECTRICITY_PRODUCTION,
                start = Instant.parse("2022-01-01T00:00:00Z"),
                values = FloatArray(385) { 0.0f } + floatArrayOf(1.2f, 2.2f)// 385 nulls, exceeding limit
            )
        )
        val result = electricityValidator.validateQuarterHourlyProductionData(electricity)
        assertEquals(Status.INVALID, result.status)
        assertEquals("Quarter-hourly production data has a gap of 96 hours, exceeding the allowed limit", result.message)
    }

    @Test
    fun validateQuarterHourlyDeliveryDataExactLimit() {
        val electricity = Electricity(
            quarterHourlyDelivery_kWh = TimeSeries(
                type = TimeSeriesType.ELECTRICITY_DELIVERY,
                start = Instant.parse("2022-01-01T00:00:00Z"),
                values = FloatArray(384) { 0.0f } + floatArrayOf(1.2f, 2.2f) // 384 nulls, at the limit
            )
        )
        val result = electricityValidator.validateQuarterHourlyDeliveryData(electricity)
        assertEquals(Status.VALID, result.status)
        assertEquals("Quarter-hourly delivery data has no gaps exceeding the limit", result.message)
    }
}
