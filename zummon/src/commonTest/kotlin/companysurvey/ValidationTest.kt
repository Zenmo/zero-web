package companysurvey

import com.benasher44.uuid.uuid4
import com.zenmo.zummon.companysurvey.*
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours

class ValidationTest {
    @Test
    fun loadSurvey() {
        assertEquals(mockSurvey.companyName, "Zenmo")
        assertTrue(mockSurvey.dataSharingAgreed)
    }

    @Test
    fun validateContractedCapacity() {
        var results = validateContractedCapacity.validate(mockSurvey)
        assertEquals(results.status, Status.VALID)
        assertContains(results.message, "valid")

        var wipeGrootverbruik = CompanyGrootverbruik(
            contractedConnectionDeliveryCapacity_kW = null,
            physicalCapacityKw = null,
        )
        var mockSurveySample = updateCapacity(wipeGrootverbruik)
        results = validateContractedCapacity.validate(mockSurveySample)
        assertEquals(results.status, Status.MISSING_DATA)
        assertContains(results.message, "is not provided")

        val invalidGrootverbruik = CompanyGrootverbruik(
            contractedConnectionDeliveryCapacity_kW = 300,
            physicalCapacityKw = 100,
        )
        mockSurveySample = updateCapacity(invalidGrootverbruik)
        results = validateContractedCapacity.validate(mockSurveySample)
        assertEquals(results.status, Status.INVALID)
        assertContains(results.message, "exceeds")
    }

    @Test
    fun validValidations() {
        val mockSurvey = createMockSurvey()

        // Test for contracted delivery capacity
        var result = validateContractedCapacity.validate(mockSurvey)
        assertEquals(result.status, Status.VALID)

        // Test for contracted feed-in capacity
        result = validateContractedFeedInCapacity.validate(mockSurvey)
        assertEquals(result.status, Status.VALID)

        // Test for PV production
        result = validatePvProduction.validate(mockSurvey)
        assertEquals(result.status, Status.VALID)

        // Test for grootverbruik physical capacity
        result = validateGrootverbruikPhysicalCapacity.validate(mockSurvey)
        assertEquals(result.status, Status.VALID)

        // Test for kleinverbruik physical capacity
        result = validateKleinverbruikPhysicalCapacity.validate(mockSurvey)
        assertEquals(result.status, Status.VALID)

        // Test for power per charge point
        result = validatePowerPerChargeCars.validate(mockSurvey)
        assertEquals(result.status, Status.VALID)

        // Test for total power of charge points
        result = validateTotalPowerChargePoints.validate(mockSurvey)
        assertEquals(result.status, Status.VALID)

        // Test for vehicle travel distance
        result = validateCarTravelDistance.validate(mockSurvey)
        assertEquals(result.status, Status.VALID)

        // Test for number of electric vehicles
        result = validateTotalElectricCars.validate(mockSurvey)
        assertEquals(result.status, Status.VALID)
    }

    @Test
    fun testInvalidValidations() {
        val invalidSurvey = updateMockSurveyWithInvalidData()

        // Test for contracted delivery capacity (should fail)
        var result = validateContractedCapacity.validate(invalidSurvey)
        assertEquals(result.status, Status.INVALID)
        assertContains(result.message, "Contracted delivery capacity exceeds physical capacity")

        // Test for contracted feed-in capacity (should fail)
        result = validateContractedFeedInCapacity.validate(invalidSurvey)
        assertEquals(result.status, Status.INVALID)
        assertContains(result.message, "Feed-in capacity exceeds physical capacity")

        // Test for PV production (should fail)
        result = validatePvProduction.validate(invalidSurvey)
        assertEquals(result.status, Status.INVALID)
        assertContains(result.message, "PV production is less than feed-in")

        // Test for grootverbruik physical capacity (should fail)
        result = validateGrootverbruikPhysicalCapacity.validate(invalidSurvey)
        assertEquals(result.status, Status.INVALID)
        assertContains(result.message, "Grootverbruik physical capacity is below 3x80A")

        // Test for kleinverbruik physical capacity (should fail)
        result = validateKleinverbruikPhysicalCapacity.validate(invalidSurvey)
        assertEquals(result.status, Status.INVALID)
        assertContains(result.message, "Kleinverbruik physical capacity exceeds 3x80A")

        // Test for power per charge point (should fail)
        result = validatePowerPerChargeCars.validate(invalidSurvey)
        assertEquals(result.status, Status.INVALID)
        assertContains(result.message, "power per charge point is outside the valid range")

        // Test for power per charge point (should fail)
        result = validatePowerPerChargeTrucks.validate(invalidSurvey)
        assertEquals(result.status, Status.NOT_APPLICABLE)
        assertContains(result.message, "is not provided")

        // Test for total power of charge points (should fail, contracted capacity + battery is too low)
        result = validateTotalPowerChargePoints.validate(invalidSurvey)
        assertEquals(result.status, Status.INVALID)
        assertContains(result.message, "power of charge points exceeds allowed capacity")

        // Test for vehicle travel distance (should fail)
        result = validateCarTravelDistance.validate(invalidSurvey)
        assertEquals(result.status, Status.INVALID)
        assertContains(result.message, "travel distance are outside the valid range")

        // Test for number of electric vehicles
        result = validateTotalElectricVans.validate(mockSurvey)
        assertEquals(result.status, Status.INVALID)
        assertContains(result.message, "exceeds the total")
    }

}

