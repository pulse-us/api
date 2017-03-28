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
import gov.ca.emsa.pulse.broker.dto.PulseEventActionCodeDTO;
import gov.ca.emsa.pulse.broker.dto.PulseEventActionDTO;
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
import org.springframework.test.annotation.Rollback;
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
	
	@Autowired private EventActionCodeDAO eventActionCodeDao;
	@Autowired private NetworkAccessPointTypeCodeDAO networkAccessPointTypeCodeDao;
	@Autowired private ParticipantObjectTypeCodeDAO participantObjectTypeCodeDao;
	@Autowired private ParticipantObjectTypeCodeRoleDAO participantObjectTypeCodeRoleDao;
	@Autowired private PulseEventActionCodeDAO pulseEventActionCodeDao;
	@Autowired private PulseEventActionDAO pulseEventActionDao;
	private static String PATIENT_CREATION_ACTION_JSON = "{'id':2,'locationPatientId':null,'fullName':'Brian Lindsey','friendlyName':null,'dateOfBirth':1483246800000,'gender':'M','phoneNumber':null,'ssn':null,'lastRead':1485787441786,'acf':{'id':1,'name':'Alameda-01','phoneNumber':null,'address':null,'lastRead':1485787441792},'locationMaps':[]}";
	private static String DOCUMENT_VIEW_ACTION_JSON = "{'documentIdentifier':{'homeCommunityId':'urn:oid:2.16.840.1.113883.3.166','repositoryUniqueId':'2.16.840.1.113883.3.166.3.1','documentUniqueId':'129.6.58.92.146'},'format':'HL7 CCD Document','name':'Hospital Admission','className':'ALLERGY NOTE','confidentiality':'High','description':null,'size':'35400','creationTime':'20080515'}";
	private static String PATIENT_DISCHARGE_ACTION_JSON = "{'id':2,'locationPatientId':null,'fullName':'Brian Lindsey','friendlyName':null,'dateOfBirth':1483246800000,'gender':'M','phoneNumber':null,'ssn':null,'lastRead':1485787441786,'acf':{'id':1,'name':'Alameda-01','phoneNumber':null,'address':null,'lastRead':1485787441792},'locationMaps':[]}";
	
	@Test
	@Transactional
	@Rollback(true)
	public void testPulseActionEventPC() throws UnknownHostException {
		
		PulseEventActionDTO pulseEventActionDTO = new PulseEventActionDTO();
		pulseEventActionDTO.setPulseEventActionCodeId(pulseEventActionCodeDao.getByCode("PC").getId());
		pulseEventActionDTO.setActionJson(PATIENT_CREATION_ACTION_JSON);
		pulseEventActionDTO.setActionTStamp(new Date());
		pulseEventActionDTO.setUsername("blindsey");
		pulseEventActionDTO.setLastModifiedDate(new Date());
		
		PulseEventActionDTO insertedAuditEvent = pulseEventActionDao.create(pulseEventActionDTO);
		
		assertNotNull(insertedAuditEvent);
		assertEquals(pulseEventActionDTO.getPulseEventActionCodeId(), insertedAuditEvent.getPulseEventActionCodeId());
		assertEquals(pulseEventActionDTO.getActionJson(), insertedAuditEvent.getActionJson());
		assertEquals(pulseEventActionDTO.getActionTStamp(), insertedAuditEvent.getActionTStamp());
		assertEquals(pulseEventActionDTO.getUsername(), insertedAuditEvent.getUsername());
		assertEquals(pulseEventActionDTO.getLastModifiedDate(), insertedAuditEvent.getLastModifiedDate());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testPulseActionEventPD() throws UnknownHostException {
		
		PulseEventActionDTO pulseEventActionDTO = new PulseEventActionDTO();
		pulseEventActionDTO.setPulseEventActionCodeId(pulseEventActionCodeDao.getByCode("PD").getId());
		pulseEventActionDTO.setActionJson(PATIENT_DISCHARGE_ACTION_JSON);
		pulseEventActionDTO.setActionTStamp(new Date());
		pulseEventActionDTO.setUsername("blindsey");
		pulseEventActionDTO.setLastModifiedDate(new Date());
		
		PulseEventActionDTO insertedAuditEvent = pulseEventActionDao.create(pulseEventActionDTO);
		
		assertNotNull(insertedAuditEvent);
		assertEquals(pulseEventActionDTO.getPulseEventActionCodeId(), insertedAuditEvent.getPulseEventActionCodeId());
		assertEquals(pulseEventActionDTO.getActionJson(), insertedAuditEvent.getActionJson());
		assertEquals(pulseEventActionDTO.getActionTStamp(), insertedAuditEvent.getActionTStamp());
		assertEquals(pulseEventActionDTO.getUsername(), insertedAuditEvent.getUsername());
		assertEquals(pulseEventActionDTO.getLastModifiedDate(), insertedAuditEvent.getLastModifiedDate());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testPulseActionEventDV() throws UnknownHostException {
		
		PulseEventActionDTO pulseEventActionDTO = new PulseEventActionDTO();
		pulseEventActionDTO.setPulseEventActionCodeId(pulseEventActionCodeDao.getByCode("DV").getId());
		pulseEventActionDTO.setActionJson(DOCUMENT_VIEW_ACTION_JSON);
		pulseEventActionDTO.setActionTStamp(new Date());
		pulseEventActionDTO.setUsername("blindsey");
		pulseEventActionDTO.setLastModifiedDate(new Date());
		
		PulseEventActionDTO insertedAuditEvent = pulseEventActionDao.create(pulseEventActionDTO);
		
		assertNotNull(insertedAuditEvent);
		assertEquals(pulseEventActionDTO.getPulseEventActionCodeId(), insertedAuditEvent.getPulseEventActionCodeId());
		assertEquals(pulseEventActionDTO.getActionJson(), insertedAuditEvent.getActionJson());
		assertEquals(pulseEventActionDTO.getActionTStamp(), insertedAuditEvent.getActionTStamp());
		assertEquals(pulseEventActionDTO.getUsername(), insertedAuditEvent.getUsername());
		assertEquals(pulseEventActionDTO.getLastModifiedDate(), insertedAuditEvent.getLastModifiedDate());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testInsertAuditPatientDiscovery() throws UnknownHostException {
		
		AuditEventDTO auditEventDTO = new AuditEventDTO();
		auditEventDTO.setEventActionCodeId(eventActionCodeDao.getByCode("E").getId());
		auditEventDTO.setEventId("EV(110112, DCM, “Query”)");
		auditEventDTO.setEventDateTime(new Date().toString());
		auditEventDTO.setEventTypeCode("EV(“ITI-55”, “IHE Transactions”, “Cross Gateway Patient Discovery”)");
		
		AuditRequestSourceDTO auditRequestSourceDTO = new AuditRequestSourceDTO();
		auditRequestSourceDTO.setUserId("");
		auditRequestSourceDTO.setAlternativeUserId(ManagementFactory.getRuntimeMXBean().getName());
		auditRequestSourceDTO.setNetworkAccessPointId(InetAddress.getLocalHost().toString());
		auditRequestSourceDTO.setNetworkAccessPointTypeCodeId(networkAccessPointTypeCodeDao.getByCode("2").getId());
		auditRequestSourceDTO.setRoleIdCode("EV(110153, DCM, “Source”)");
		auditRequestSourceDTO.setUserIsRequestor(true);
		auditEventDTO.setAuditRequestSource(auditRequestSourceDTO);
		
		AuditRequestDestinationDTO auditRequestDestinationDTO = new AuditRequestDestinationDTO();
		auditRequestDestinationDTO.setUserId("https://www.someihe.com/patientDiscovery");
		auditRequestDestinationDTO.setUserIsRequestor(true);
		auditRequestDestinationDTO.setRoleIdCode("EV(110152, DCM, “Destination”)");
		auditRequestDestinationDTO.setNetworkAccessPointTypeCodeId(networkAccessPointTypeCodeDao.getByCode("2").getId());
		auditRequestDestinationDTO.setNetworkAccessPointId(InetAddress.getLocalHost().toString());
		auditEventDTO.setAuditRequestDestination(auditRequestDestinationDTO);
		
		AuditSourceDTO auditSourceDTO = new AuditSourceDTO();
		auditSourceDTO.setAuditEnterpriseSiteId("Cal EMSA");
		auditSourceDTO.setAuditSourceTypeCode("Emergency");
		auditEventDTO.setAuditSource(auditSourceDTO);
		
		AuditQueryParametersDTO auditQueryParametersDTO = new AuditQueryParametersDTO();
		auditQueryParametersDTO.setParticipantObjectTypeCodeId(participantObjectTypeCodeDao.getByCode("2").getId());
		auditQueryParametersDTO.setParticipantObjectTypeCodeRoleId(participantObjectTypeCodeRoleDao.getByCode("24").getId());
		auditQueryParametersDTO.setParticipantObjectIdTypeCode("EV(“ITI-55, “IHE Transactions”, “Cross Gateway Patient Discovery”)");
		auditQueryParametersDTO.setParticipantObjectQuery("DBKHOIUDFIFO*G#(*OFDGTD(&#GIUFDOUFD(UG(GFDH");
		auditQueryParametersDTO.setParticipantObjectDetail("urn:uuid:a02ca8cd-86fa-4afc-a27c-616c183b2055"); // homeCommunityId
		auditEventDTO.setAuditQueryParameters(auditQueryParametersDTO);
		
		AuditHumanRequestorDTO auditHumanRequestorDTO = new AuditHumanRequestorDTO();
		auditHumanRequestorDTO.setUserId("https://www.someihe.com/patientDiscovery");
		auditHumanRequestorDTO.setUserIsRequestor(true);
		auditHumanRequestorDTO.setRoleIdCode("EV(110152, DCM, “Destination”)");
		auditHumanRequestorDTO.setNetworkAccessPointTypeCode(networkAccessPointTypeCodeDao.getByCode("2"));
		auditHumanRequestorDTO.setNetworkAccessPointTypeCodeId(networkAccessPointTypeCodeDao.getByCode("2").getId());
		auditHumanRequestorDTO.setNetworkAccessPointId(InetAddress.getLocalHost().toString());
		ArrayList<AuditHumanRequestorDTO> humanRequestors = new ArrayList<AuditHumanRequestorDTO>();
		humanRequestors.add(auditHumanRequestorDTO);
		auditEventDTO.setAuditHumanRequestors(humanRequestors);
		
		AuditEventDTO insertedAuditEvent = auditDao.createAuditEvent(auditEventDTO);
		
		//AuditEventDTO auditEvent = auditDao.getAuditEventById(insertedAuditEvent.getId());
		
		//assertNotNull(auditEvent);
		
		assertNotNull(insertedAuditEvent);
		assertEquals(auditEventDTO.getEventTypeCode(), insertedAuditEvent.getEventTypeCode());
		assertEquals(auditEventDTO.getAuditRequestSource().getNetworkAccessPointId(), insertedAuditEvent.getAuditRequestSource().getNetworkAccessPointId());
		assertEquals(auditEventDTO.getAuditRequestDestination().getRoleIdCode(), insertedAuditEvent.getAuditRequestDestination().getRoleIdCode());
		assertEquals(auditEventDTO.getAuditSource().getAuditEnterpriseSiteId(), insertedAuditEvent.getAuditSource().getAuditEnterpriseSiteId());
		assertEquals(auditEventDTO.getAuditQueryParameters().getParticipantObjectIdTypeCode(), insertedAuditEvent.getAuditQueryParameters().getParticipantObjectIdTypeCode());
		assertEquals(auditEventDTO.getAuditHumanRequestors().get(0).getNetworkAccessPointId(), insertedAuditEvent.getAuditHumanRequestors().get(0).getNetworkAccessPointId());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testInsertAuditDocumentQuery() throws UnknownHostException {
		
		AuditEventDTO auditEventDTO = new AuditEventDTO();
		auditEventDTO.setEventActionCodeId(eventActionCodeDao.getByCode("C").getId());
		auditEventDTO.setEventId("EV(110107, DCM, “Import”)");
		auditEventDTO.setEventDateTime(new Date().toString());
		auditEventDTO.setEventTypeCode("EV(“ITI-38”, “IHE Transactions”, and “Cross Gateway Query”)");
		
		AuditRequestSourceDTO auditRequestSourceDTO = new AuditRequestSourceDTO();
		auditRequestSourceDTO.setUserId("");
		auditRequestSourceDTO.setAlternativeUserId(ManagementFactory.getRuntimeMXBean().getName());
		auditRequestSourceDTO.setNetworkAccessPointId(InetAddress.getLocalHost().toString());
		auditRequestSourceDTO.setNetworkAccessPointTypeCodeId(networkAccessPointTypeCodeDao.getByCode("2").getId());
		auditRequestSourceDTO.setRoleIdCode("EV(110153, DCM, “Source”)");
		auditRequestSourceDTO.setUserIsRequestor(true);
		auditEventDTO.setAuditRequestSource(auditRequestSourceDTO);
		
		AuditRequestDestinationDTO auditRequestDestinationDTO = new AuditRequestDestinationDTO();
		auditRequestDestinationDTO.setUserId("https://www.someihe.com/patientDiscovery");
		auditRequestDestinationDTO.setUserIsRequestor(true);
		auditRequestDestinationDTO.setRoleIdCode("EV(110152, DCM, “Destination”)");
		auditRequestDestinationDTO.setNetworkAccessPointTypeCodeId(networkAccessPointTypeCodeDao.getByCode("2").getId());
		auditRequestDestinationDTO.setNetworkAccessPointId(InetAddress.getLocalHost().toString());
		auditEventDTO.setAuditRequestDestination(auditRequestDestinationDTO);
		
		AuditSourceDTO auditSourceDTO = new AuditSourceDTO();
		auditSourceDTO.setAuditEnterpriseSiteId("Cal EMSA");
		auditSourceDTO.setAuditSourceTypeCode("Emergency");
		auditEventDTO.setAuditSource(auditSourceDTO);
		
		AuditPatientDTO auditPatientDTO = new AuditPatientDTO();
		auditPatientDTO.setParticipantObjectTypeCodeId(participantObjectTypeCodeDao.getByCode("1").getId());
		auditPatientDTO.setParticipantObjectTypeCodeRoleId(participantObjectTypeCodeRoleDao.getByCode("1").getId());
		auditPatientDTO.setParticipantObjectIdTypeCode("");
		auditPatientDTO.setParticipantObjectQuery("");
		auditPatientDTO.setParticipantObjectDetail("");
		auditEventDTO.setAuditPatient(auditPatientDTO);
		
		AuditHumanRequestorDTO auditHumanRequestorDTO = new AuditHumanRequestorDTO();
		auditHumanRequestorDTO.setUserId(UserUtil.getCurrentUser().getFirstName());
		auditHumanRequestorDTO.setUserIsRequestor(true);
		auditHumanRequestorDTO.setRoleIdCode("EV(110152, DCM, “Destination”)");
		auditHumanRequestorDTO.setNetworkAccessPointTypeCodeId(networkAccessPointTypeCodeDao.getByCode("2").getId());
		auditHumanRequestorDTO.setNetworkAccessPointId(InetAddress.getLocalHost().toString());
		ArrayList<AuditHumanRequestorDTO> humanRequestors = new ArrayList<AuditHumanRequestorDTO>();
		humanRequestors.add(auditHumanRequestorDTO);
		auditEventDTO.setAuditHumanRequestors(humanRequestors);
		
		AuditDocumentDTO auditDocumentDTO = new AuditDocumentDTO();
		auditDocumentDTO.setParticipantObjectTypeCodeId(participantObjectTypeCodeDao.getByCode("2").getId());
		auditDocumentDTO.setParticipantObjectTypeCodeRoleId(participantObjectTypeCodeRoleDao.getByCode("3").getId());
		auditDocumentDTO.setParticipantObjectIdTypeCode("");
		auditDocumentDTO.setParticipantObjectId("urn:id:blahblahblah");
		auditDocumentDTO.setParticipantObjectQuery("urn:oid:1.2.3.928.955");
		auditDocumentDTO.setParticipantObjectDetail("urn:oid:1.2.3.928.955");
		ArrayList<AuditDocumentDTO> auditDocuments = new ArrayList<AuditDocumentDTO>();
		auditDocuments.add(auditDocumentDTO);
		auditEventDTO.setAuditDocument(auditDocuments);
		
		AuditEventDTO insertedAuditEvent = auditDao.createAuditEvent(auditEventDTO);
		
		//AuditEventDTO auditEvent = auditDao.getAuditEventById(insertedAuditEvent.getId());
		
		//assertNotNull(auditEvent);
		
		assertNotNull(insertedAuditEvent);
		assertEquals(auditEventDTO.getEventTypeCode(), insertedAuditEvent.getEventTypeCode());
		assertEquals(auditEventDTO.getAuditRequestSource().getNetworkAccessPointId(), insertedAuditEvent.getAuditRequestSource().getNetworkAccessPointId());
		assertEquals(auditEventDTO.getAuditRequestDestination().getRoleIdCode(), insertedAuditEvent.getAuditRequestDestination().getRoleIdCode());
		assertEquals(auditEventDTO.getAuditSource().getAuditEnterpriseSiteId(), insertedAuditEvent.getAuditSource().getAuditEnterpriseSiteId());
		assertEquals(auditEventDTO.getAuditHumanRequestors().get(0).getNetworkAccessPointId(), insertedAuditEvent.getAuditHumanRequestors().get(0).getNetworkAccessPointId());
		assertEquals(auditEventDTO.getAuditPatient().getParticipantObjectName(), insertedAuditEvent.getAuditPatient().getParticipantObjectName());
		assertEquals(auditEventDTO.getAuditDocument().get(0).getParticipantObjectId(), insertedAuditEvent.getAuditDocument().get(0).getParticipantObjectId());
	}
}
