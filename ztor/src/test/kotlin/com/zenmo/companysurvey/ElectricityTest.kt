package com.zenmo.companysurvey

import com.zenmo.companysurvey.dto.CompanyGrootverbruik
import com.zenmo.companysurvey.dto.CompanyKleinverbruik
import com.zenmo.companysurvey.dto.Electricity
import com.zenmo.companysurvey.dto.KleinverbruikElectricityConnectionCapacity
import kotlin.test.Test
import kotlin.test.assertEquals

class ElectricityTest {
    @Test
    fun testKleinverbruikConnectionCapacity() {
        val electricity = Electricity(
            kleinverbruik = CompanyKleinverbruik(
                connectionCapacity = KleinverbruikElectricityConnectionCapacity.`3x80A`,
            )
        )

        assertEquals(electricity.getConnectionCapacityKw(), 3 * 80)
    }

    @Test
    fun testGrootverbruikConnectionCapacity() {
        val electricity = Electricity(
            grootverbruik = CompanyGrootverbruik(
                contractedConnectionDemandCapacityKw = 400
            )
        )

        assertEquals(electricity.getConnectionCapacityKw(), 400)
    }
}
