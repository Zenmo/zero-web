import com.zenmo.vallum.Vallum;
import com.zenmo.zummon.companysurvey.Survey;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VallumTest {
    @Test
    public void test() {
        // TODO: start Ztor server here and populate entities so we can run more self-contained.
        // Needs refactoring of Ztor to allow for this.
        Vallum vallum = new Vallum(
                "test-client-ztor-api",
                "iWdpSK2jP0Rw1v8I2b1SQmZ4mI6LwNRy",
                "http://ztor-run:8082",
                "https://keycloak.zenmo.com/realms/testrealm/protocol/openid-connect/token"
        );
        List<Survey> surveys = vallum.getSurveysByProject("hessenpoort");
        assertEquals(28, surveys.size());
    }
}
