package com.zenmo.orm.companysurvey;

import com.zenmo.zummon.companysurvey.Address;
import org.junit.Test;
import static org.junit.Assert.*;

public class InteropTest {
    @Test
    public void testUintCompatibility() {
        var mocksurvey = com.zenmo.orm.companysurvey.MockSurveyKt.getMockSurvey();
        Address address = mocksurvey.getAddresses().get(0);
        int houseNumber = address.getHouseNumber();
        assertEquals(35, houseNumber);
    }
}
