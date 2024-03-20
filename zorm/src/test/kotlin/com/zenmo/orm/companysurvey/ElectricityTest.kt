package com.zenmo.orm.companysurvey

import com.zenmo.orm.companysurvey.dto.CompanyGrootverbruik
import com.zenmo.orm.companysurvey.dto.CompanyKleinverbruik
import com.zenmo.orm.companysurvey.dto.Electricity
import com.zenmo.orm.companysurvey.dto.KleinverbruikElectricityConnectionCapacity
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

        assertEquals(electricity.getContractedConnectionCapacityKw(), 3 * 80)
    }

    @Test
    fun testGrootverbruikConnectionCapacity() {
        val electricity = Electricity(
            grootverbruik = CompanyGrootverbruik(
                contractedConnectionDemandCapacityKw = 400
            )
        )

        assertEquals(electricity.getContractedConnectionCapacityKw(), 400)
    }
}
