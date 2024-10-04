package com.zenmo.orm.companysurvey;

import com.zenmo.zummon.companysurvey.Address;
import com.zenmo.zummon.companysurvey.GridConnection;
import com.zenmo.zummon.companysurvey.PandID;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.time.Instant;
import java.util.Set;

import static org.junit.Assert.*;

public class InteropTest {
    @Test
    public void testUintCompatibility() {
        var mocksurvey = com.zenmo.orm.companysurvey.MockSurveyKt.getMockSurvey();
        Address address = mocksurvey.getAddresses().get(0);
        int houseNumber = address.getHouseNumber();
        assertEquals(35, houseNumber);

        GridConnection gc = address.getGridConnections().get(0);
        Set<PandID> pandIds = gc.getPandIds();
        System.out.println(pandIds);

        var timeSeries = gc.getElectricity().getQuarterHourlyFeedIn_kWh();

        var isEmpty = timeSeries.getValues().length == 0;

        Instant javaStart = timeSeries.getStart().toJavaInstant();
        System.out.println(javaStart);
        System.out.println(javaStart.isBefore(Instant.parse("2023-01-01T01:00:00+01:00")));
    }
}
