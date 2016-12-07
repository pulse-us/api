package gov.ca.emsa.pulse.broker.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.audit.AuditHumanRequestor;
import gov.ca.emsa.pulse.broker.domain.QueryType;
import gov.ca.emsa.pulse.broker.dto.AuditDocumentDTO;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditHumanRequestorDTO;
import gov.ca.emsa.pulse.broker.dto.AuditPatientDTO;
import gov.ca.emsa.pulse.broker.dto.AuditQueryParametersDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestDestinationDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestSourceDTO;
import gov.ca.emsa.pulse.broker.dto.AuditSourceDTO;
import gov.ca.emsa.pulse.broker.manager.AuditEventManager;
import gov.ca.emsa.pulse.service.UserUtil;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
public class AuditDaoTest {
	
	@Autowired AuditEventDAO auditDao;
	
	@Autowired AuditHumanRequestorDAO auditHumanRequestorDAO;
	@Autowired AuditPatientDAO auditPatientDAO;
	@Autowired AuditQueryParametersDAO auditQueryParametersDAO;
	@Autowired AuditRequestDestinationDAO auditRequestDestinationDAO;
	@Autowired AuditRequestSourceDAO auditRequestSourceDAO;
	@Autowired AuditSourceDAO auditSourceDAO;
	
	@Test
	@Transactional
	public void testInsertAuditPatientDiscovery() throws UnknownHostException {
		
		AuditEventDTO auditEventDTO = new AuditEventDTO();
		auditEventDTO.setEventActionCode("E");
		auditEventDTO.setEventId("EV(110112, DCM, “Query”)");
		auditEventDTO.setEventDateTime(new Date().toString());
		auditEventDTO.setEventTypeCode("EV(“ITI-55”, “IHE Transactions”, “Cross Gateway Patient Discovery”)");
		
		AuditRequestSourceDTO auditRequestSourceDTO = new AuditRequestSourceDTO();
		auditRequestSourceDTO.setUserId(""); // i dunno if this is right
		auditRequestSourceDTO.setAlternativeUserId(ManagementFactory.getRuntimeMXBean().getName());
		auditRequestSourceDTO.setNetworkAccessPointId(InetAddress.getLocalHost().toString());
		auditRequestSourceDTO.setNetworkAccessPointTypeCode("2");
		auditRequestSourceDTO.setRoleIdCode("EV(110153, DCM, “Source”)");
		auditRequestSourceDTO.setUserIsRequestor(true);
		auditEventDTO.setAuditRequestSource(auditRequestSourceDTO);
		
		AuditRequestDestinationDTO auditRequestDestinationDTO = new AuditRequestDestinationDTO();
		auditRequestDestinationDTO.setUserId("https://www.someihe.com/patientDiscovery");
		auditRequestDestinationDTO.setUserIsRequestor(true);
		auditRequestDestinationDTO.setRoleIdCode("EV(110152, DCM, “Destination”)");
		auditRequestDestinationDTO.setNetworkAccessPointTypeCode("2");
		auditRequestDestinationDTO.setNetworkAccessPointId(InetAddress.getLocalHost().toString());
		auditEventDTO.setAuditRequestDestination(auditRequestDestinationDTO);
		
		AuditSourceDTO auditSourceDTO = new AuditSourceDTO();
		auditSourceDTO.setAuditEnterpriseSiteId("Cal EMSA");
		auditSourceDTO.setAuditSourceTypeCode("Emergency");
		auditEventDTO.setAuditSource(auditSourceDTO);
		
		AuditQueryParametersDTO auditQueryParametersDTO = new AuditQueryParametersDTO();
		auditQueryParametersDTO.setParticipantObjectTypeCode(2);
		auditQueryParametersDTO.setParticipantObjectTypeCodeRole(24);
		auditQueryParametersDTO.setParticipantObjectIdTypeCode("EV(“ITI-55, “IHE Transactions”, “Cross Gateway Patient Discovery”)");
		auditQueryParametersDTO.setParticipantObjectQuery("DBKHOIUDFIFO*G#(*OFDGTD(&#GIUFDOUFD(UG(GFDH");
		auditQueryParametersDTO.setParticipantObjectDetail("urn:uuid:a02ca8cd-86fa-4afc-a27c-616c183b2055"); // homeCommunityId
		auditEventDTO.setAuditQueryParameters(auditQueryParametersDTO);
		
		AuditPatientDTO auditPatientDTO = new AuditPatientDTO();
		auditPatientDTO.setParticipantObjectTypeCode(2);
		auditPatientDTO.setParticipantObjectTypeCodeRole(24);
		auditPatientDTO.setParticipantObjectIdTypeCode("EV(“ITI-55, “IHE Transactions”, “Cross Gateway Patient Discovery”)");
		auditPatientDTO.setParticipantObjectQuery("DBKHOIUDFIFO*G#(*OFDGTD(&#GIUFDOUFD(UG(GFDH");
		auditPatientDTO.setParticipantObjectDetail("urn:uuid:a02ca8cd-86fa-4afc-a27c-616c183b2055"); // homeCommunityId
		auditEventDTO.setAuditPatient(auditPatientDTO);
		
		AuditHumanRequestorDTO auditHumanRequestorDTO = new AuditHumanRequestorDTO();
		auditHumanRequestorDTO.setUserId("https://www.someihe.com/patientDiscovery");
		auditHumanRequestorDTO.setUserIsRequestor(true);
		auditHumanRequestorDTO.setRoleIdCode("EV(110152, DCM, “Destination”)");
		auditHumanRequestorDTO.setNetworkAccessPointTypeCode("2");
		auditHumanRequestorDTO.setNetworkAccessPointId(InetAddress.getLocalHost().toString());
		ArrayList<AuditHumanRequestorDTO> humanRequestors = new ArrayList<AuditHumanRequestorDTO>();
		humanRequestors.add(auditHumanRequestorDTO);
		auditEventDTO.setAuditHumanRequestors(humanRequestors);
		
		AuditEventDTO insertedAuditEvent = auditDao.createAuditEvent(auditEventDTO);
		
		assertNotNull(insertedAuditEvent);
		assertEquals(auditEventDTO.getEventTypeCode(), insertedAuditEvent.getEventTypeCode());
		assertEquals(auditEventDTO.getAuditRequestSource().getNetworkAccessPointId(), insertedAuditEvent.getAuditRequestSource().getNetworkAccessPointId());
		assertEquals(auditEventDTO.getAuditRequestDestination().getRoleIdCode(), insertedAuditEvent.getAuditRequestDestination().getRoleIdCode());
		assertEquals(auditEventDTO.getAuditSource().getAuditEnterpriseSiteId(), insertedAuditEvent.getAuditSource().getAuditEnterpriseSiteId());
		assertEquals(auditEventDTO.getAuditQueryParameters().getParticipantObjectIdTypeCode(), insertedAuditEvent.getAuditQueryParameters().getParticipantObjectIdTypeCode());
		assertEquals(auditEventDTO.getAuditHumanRequestors().get(0).getNetworkAccessPointId(), insertedAuditEvent.getAuditHumanRequestors().get(0).getNetworkAccessPointId());
		assertEquals(auditEventDTO.getAuditPatient().getParticipantObjectName(), insertedAuditEvent.getAuditPatient().getParticipantObjectName());

	}
	
	@Test
	@Transactional
	public void testInsertAuditDocumentQuery() throws UnknownHostException {
		
		AuditEventDTO auditEventDTO = new AuditEventDTO();
		auditEventDTO.setEventActionCode("C");
		auditEventDTO.setEventId("EV(110107, DCM, “Import”)");
		auditEventDTO.setEventDateTime(new Date().toString());
		auditEventDTO.setEventTypeCode("EV(“ITI-38”, “IHE Transactions”, and “Cross Gateway Query”)");
		
		AuditRequestSourceDTO auditRequestSourceDTO = new AuditRequestSourceDTO();
		auditRequestSourceDTO.setUserId(""); // i dunno if this is right
		auditRequestSourceDTO.setAlternativeUserId(ManagementFactory.getRuntimeMXBean().getName());
		auditRequestSourceDTO.setNetworkAccessPointId(InetAddress.getLocalHost().toString());
		auditRequestSourceDTO.setNetworkAccessPointTypeCode("2");
		auditRequestSourceDTO.setRoleIdCode("EV(110153, DCM, “Source”)");
		auditRequestSourceDTO.setUserIsRequestor(true);
		auditEventDTO.setAuditRequestSource(auditRequestSourceDTO);
		
		AuditRequestDestinationDTO auditRequestDestinationDTO = new AuditRequestDestinationDTO();
		auditRequestDestinationDTO.setUserId("https://www.someihe.com/patientDiscovery");
		auditRequestDestinationDTO.setUserIsRequestor(true);
		auditRequestDestinationDTO.setRoleIdCode("EV(110152, DCM, “Destination”)");
		auditRequestDestinationDTO.setNetworkAccessPointTypeCode("2");
		auditRequestDestinationDTO.setNetworkAccessPointId(InetAddress.getLocalHost().toString());
		auditEventDTO.setAuditRequestDestination(auditRequestDestinationDTO);
		
		AuditSourceDTO auditSourceDTO = new AuditSourceDTO();
		auditSourceDTO.setAuditEnterpriseSiteId("Cal EMSA");
		auditSourceDTO.setAuditSourceTypeCode("Emergency");
		auditEventDTO.setAuditSource(auditSourceDTO);
		
		AuditPatientDTO auditPatientDTO = new AuditPatientDTO();
		auditPatientDTO.setParticipantObjectTypeCode(1);
		auditPatientDTO.setParticipantObjectTypeCodeRole(1);
		auditPatientDTO.setParticipantObjectIdTypeCode("");
		auditPatientDTO.setParticipantObjectQuery("");
		auditPatientDTO.setParticipantObjectDetail("");
		auditEventDTO.setAuditPatient(auditPatientDTO);
		
		AuditHumanRequestorDTO auditHumanRequestorDTO = new AuditHumanRequestorDTO();
		auditHumanRequestorDTO.setUserId(UserUtil.getCurrentUser().getFirstName());
		auditHumanRequestorDTO.setUserIsRequestor(true);
		auditHumanRequestorDTO.setRoleIdCode("EV(110152, DCM, “Destination”)");
		auditHumanRequestorDTO.setNetworkAccessPointTypeCode("2");
		auditHumanRequestorDTO.setNetworkAccessPointId(InetAddress.getLocalHost().toString());
		ArrayList<AuditHumanRequestorDTO> humanRequestors = new ArrayList<AuditHumanRequestorDTO>();
		humanRequestors.add(auditHumanRequestorDTO);
		auditEventDTO.setAuditHumanRequestors(humanRequestors);
		
		AuditDocumentDTO auditDocumentDTO = new AuditDocumentDTO();
		auditDocumentDTO.setParticipantObjectTypeCode(2);
		auditDocumentDTO.setParticipantObjectTypeCodeRole(3);
		auditDocumentDTO.setParticipantObjectIdTypeCode("");
		auditDocumentDTO.setParticipantObjectId("urn:id:blahblahblah");
		auditDocumentDTO.setParticipantObjectQuery("urn:oid:1.2.3.928.955");
		auditDocumentDTO.setParticipantObjectDetail("urn:oid:1.2.3.928.955");
		auditEventDTO.setAuditDocument(auditDocumentDTO);
		
		AuditEventDTO insertedAuditEvent = auditDao.createAuditEvent(auditEventDTO);
		
		assertNotNull(insertedAuditEvent);
		assertEquals(auditEventDTO.getEventTypeCode(), insertedAuditEvent.getEventTypeCode());
		assertEquals(auditEventDTO.getAuditRequestSource().getNetworkAccessPointId(), insertedAuditEvent.getAuditRequestSource().getNetworkAccessPointId());
		assertEquals(auditEventDTO.getAuditRequestDestination().getRoleIdCode(), insertedAuditEvent.getAuditRequestDestination().getRoleIdCode());
		assertEquals(auditEventDTO.getAuditSource().getAuditEnterpriseSiteId(), insertedAuditEvent.getAuditSource().getAuditEnterpriseSiteId());
		assertEquals(auditEventDTO.getAuditHumanRequestors().get(0).getNetworkAccessPointId(), insertedAuditEvent.getAuditHumanRequestors().get(0).getNetworkAccessPointId());
		assertEquals(auditEventDTO.getAuditPatient().getParticipantObjectName(), insertedAuditEvent.getAuditPatient().getParticipantObjectName());
		assertEquals(auditEventDTO.getAuditDocument().getParticipantObjectId(), insertedAuditEvent.getAuditDocument().getParticipantObjectId());
	}
}
