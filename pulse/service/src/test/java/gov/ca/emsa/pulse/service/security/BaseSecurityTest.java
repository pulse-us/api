package gov.ca.emsa.pulse.service.security;

import java.sql.SQLException;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import gov.ca.emsa.pulse.ServiceExceptionControllerAdvice;
import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.Endpoint;
import gov.ca.emsa.pulse.common.domain.Patient;

public class BaseSecurityTest {
    protected MockMvc mockMvc;

    protected Long acfIdUsedForTest = 1L;
    protected Patient patientUsedForTest;
    protected Document docUsedForTest;
    protected Endpoint endpointUsedForTest;

    protected Long savedAcfId;
    protected Long savedStateId;
    protected Integer totalAcfs;
    protected Long liferayStateIdUsedForTest = 10L;
    protected Long liferayAcfIdUsedForTest = 100L;
    protected String requestJsonAcf;
    protected String requestJsonAcfTest;
    protected String requestJsonPatientTest;
    protected AlternateCareFacility acfCreate;

    ObjectWriter ow;

    public void setUp(Object controller) throws SQLException, JsonProcessingException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ServiceExceptionControllerAdvice()).build();

        /*
         * prepare data set acf 1 liferay_state_id 1 liferay_state_id
         */
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ow = mapper.writer().withDefaultPrettyPrinter();
        acfCreate = new AlternateCareFacility();
        acfCreate.setName("Test Create ACF");
        acfCreate.setLiferayAcfId(1001L);
        acfCreate.setLiferayStateId(102L);
        acfCreate.setIdentifier("The ACF that is tested for create");
        requestJsonAcf = ow.writeValueAsString(acfCreate);

        patientUsedForTest = new Patient();
        requestJsonPatientTest = ow.writeValueAsString(patientUsedForTest);

        /*
         * Future
         * 
         * 
         * endpointUsedForTest = new Endpoint();
         * endpointUsedForTest.setExternalId("external"); EndpointType
         * endpointType = new EndpointType(); endpointType.setId(1L);
         * endpointUsedForTest.setEndpointType(endpointType); EndpointStatus
         * status = new EndpointStatus(); status.setId(1L);
         * endpointUsedForTest.setEndpointStatus(status);
         * endpointManager.updateEndpoints(Collections.singletonList(
         * endpointUsedForTest ));
         * 
         * DocumentDTO documentDTO = new DocumentDTO();
         * documentDTO.setDocumentUniqueId("UnitTestPulse");
         * documentDTO.setPatientEndpointMapId(1L);
         * documentDTO.setName("Name of the Doc"); docUsedForTest =
         * DtoToDomainConverter.convert(docManager.create(documentDTO));
         */
    }

    public void restore() throws SQLException {
    }
}
