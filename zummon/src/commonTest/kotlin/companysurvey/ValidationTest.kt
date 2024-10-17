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
        result = validatePowerPerChargePoint.validate(mockSurvey)
        assertEquals(result.status, Status.VALID)

        // Test for total power of charge points
        result = validateTotalPowerChargePoints.validate(mockSurvey)
        assertEquals(result.status, Status.VALID)

        // Test for vehicle travel distance
        result = validateVehicleTravelDistance.validate(mockSurvey)
        assertEquals(result.status, Status.VALID)

        // Test for number of electric vehicles
        result = validateTotalElectricVehicle.validate(mockSurvey)
        assertEquals(result.status, Status.VALID)
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
        assertContains(results.message, "No")

        val invalidGrootverbruik = CompanyGrootverbruik(
            contractedConnectionDeliveryCapacity_kW = 300,
            physicalCapacityKw = 100,
        )
        mockSurveySample = updateCapacity(invalidGrootverbruik)
        results = validateContractedCapacity.validate(mockSurveySample)
        assertEquals(results.status, Status.INVALID)
        assertContains(results.message, "exceeds")
    }
}

