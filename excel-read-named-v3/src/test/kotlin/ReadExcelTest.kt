package com.zenmo.excelreadnamed

import com.zenmo.excelreadnamed.v3.CompanyDataDocument
import kotlin.test.Test
import kotlin.test.assertNotNull

class ReadExcelTest {
    @Test
    fun testReadExcel() {
        val companyDocumented = CompanyDataDocument.fromResource("Dealnr.bedrijfsnaam.data_aanpassingenZenmo27aug.xlsx")
        val survey = companyDocumented.getSurveyObject()

        val deliveryTimeSeries = survey.getSingleGridConnection().electricity.quarterHourlyDelivery_kWh
        assertNotNull(deliveryTimeSeries)
    }
}
