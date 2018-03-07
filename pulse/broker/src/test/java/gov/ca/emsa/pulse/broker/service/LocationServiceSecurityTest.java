package gov.ca.emsa.pulse.broker.service;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.service.AlternateCareFacilityService;
import gov.ca.emsa.pulse.service.PatientService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BrokerApplicationTestConfig.class)
@WebAppConfiguration
public class LocationServiceSecurityTest extends BaseSecurityTest {
    @Autowired
    AlternateCareFacilityService acfServiceController;
    @Autowired
    PatientService patientServiceController;

    protected String patientUrlPrefix;

    @Before
    public void setUp() throws JsonProcessingException, SQLException {
        super.setUp(patientServiceController);
        patientUrlPrefix = "/patients/" + patientUsedForTest.getId();
    }

    @After
    public void restore() throws SQLException {
        super.restore();
    }

}
