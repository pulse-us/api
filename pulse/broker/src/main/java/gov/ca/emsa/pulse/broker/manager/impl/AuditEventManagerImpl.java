package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.dao.AuditEventDAO;
import gov.ca.emsa.pulse.broker.dao.EventActionCodeDAO;
import gov.ca.emsa.pulse.broker.dao.NetworkAccessPointTypeCodeDAO;
import gov.ca.emsa.pulse.broker.dao.ParticipantObjectTypeCodeDAO;
import gov.ca.emsa.pulse.broker.dao.ParticipantObjectTypeCodeRoleDAO;
import gov.ca.emsa.pulse.broker.dao.PulseEventActionCodeDAO;
import gov.ca.emsa.pulse.broker.dao.PulseEventActionDAO;
import gov.ca.emsa.pulse.broker.domain.AuditType;
import gov.ca.emsa.pulse.broker.domain.DocumentAudit;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.dto.AuditDocumentDTO;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditHumanRequestorDTO;
import gov.ca.emsa.pulse.broker.dto.AuditPatientDTO;
import gov.ca.emsa.pulse.broker.dto.AuditQueryParametersDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestDestinationDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestSourceDTO;
import gov.ca.emsa.pulse.broker.dto.AuditSourceDTO;
import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.DtoToDomainConverter;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PulseEventActionDTO;
import gov.ca.emsa.pulse.broker.manager.AlternateCareFacilityManager;
import gov.ca.emsa.pulse.broker.manager.AuditEventManager;
import gov.ca.emsa.pulse.broker.manager.DocumentManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.service.AuditUtil;
import gov.ca.emsa.pulse.service.UserUtil;

import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AuditEventManagerImpl implements AuditEventManager{

	private static final Logger logger = LogManager.getLogger(AuditEventManagerImpl.class);
	@Autowired private AuditEventDAO auditEventDao;
	@Autowired private EventActionCodeDAO eventActionCodeDao;
	@Autowired private PulseEventActionCodeDAO pulseEventActionCodeDao;
	@Autowired private PulseEventActionDAO pulseEventActionDao;
	@Autowired private NetworkAccessPointTypeCodeDAO networkAccessPointTypeCodeDao;
	@Autowired private ParticipantObjectTypeCodeDAO participantObjectTypeCodeDao;
	@Autowired private ParticipantObjectTypeCodeRoleDAO participantObjectTypeCodeRoleDao;
	@Autowired private PatientManager patientManager;
	@Autowired private DocumentManager documentManager;
	@Autowired private AlternateCareFacilityManager acfManager;
	
	private ObjectMapper mapper = new ObjectMapper();

	// Initiating gateway audit event
	@Override
	@Transactional
	public AuditEventDTO addAuditEventEntry(AuditEventDTO ae) {
		AuditEventDTO insertedAuditEventDTO = auditEventDao.createAuditEvent(ae);
		return insertedAuditEventDTO;
	}

	@Override
	@Transactional
	public List<AuditEventDTO> findAllAuditEvents() {
		return auditEventDao.findAllAuditEvents();
	}

	// create an audit event for an initiating gateway audit message
	@Transactional
	public void createAuditEventIG(String outcome, CommonUser user, String endpointUrl, String queryByParameter, String homeCommunityId) throws UnknownHostException, UnsupportedEncodingException{
		AuditRequestSourceDTO auditRequestSourceDTO = AuditUtil.createAuditRequestSource(InetAddress.getLocalHost().toString() + "/search", // not sure about this
				ManagementFactory.getRuntimeMXBean().getName().split("@")[0],
				user.getEmail(), // this is optional
				true, // this is optional
				"EV(110153, DCM, \"Source\")",
				networkAccessPointTypeCodeDao.getByCode("2").getId(),
				InetAddress.getLocalHost().toString());
		ArrayList<AuditHumanRequestorDTO> auditHumanRequestorDTO = AuditUtil.createAuditHumanRequestor(
				user.getSubjectName(),// optional
				user.getUsername(), // optional
				user.getfull_name(), // optional
				true, // optional
				user.getrole(), // optional
				null, // optional
				null);// optional
		AuditRequestDestinationDTO auditRequestDestinationDTO = AuditUtil.createAuditRequestDestination(endpointUrl,
				null, // optional
				null, // optional
				true, 
				"EV(110152, DCM, \"Destination\")", 
				networkAccessPointTypeCodeDao.getByCode("2").getId(), 
				endpointUrl);
		AuditQueryParametersDTO auditQueryParametersDTO = AuditUtil.createAuditQueryParameters(participantObjectTypeCodeDao.getByCode("2").getId(), 
				participantObjectTypeCodeRoleDao.getByCode("24").getId(), 
				null, // optional 
				"EV(\"ITI-55\", \"IHE Transactions\", \"Cross Gateway Patient Discovery\")", 
				null, // optional 
				null, // optional 
				null, // optional
				AuditUtil.base64EncodeMessage(queryByParameter), // the QueryByParameter segment of the query, base64 encoded.
				homeCommunityId); // The value of "ihe:homeCommunityID" as the value of the attribute type and the value of the homeCommunityID as the value of the attribute value.
		AuditSourceDTO auditSouceDTO = AuditUtil.createAuditSource(null, // optional 
				null, // optional 
				null); // optional
		AuditEventDTO auditEventDTO = new AuditEventDTO();
		auditEventDTO.setEventId("EV(110112, DCM, \"Query\")");
		auditEventDTO.setEventActionCodeId(eventActionCodeDao.getByCode("E").getId());
		auditEventDTO.setEventDateTime(new Date().toString());
		auditEventDTO.setEventOutcomeIndicator(outcome);
		auditEventDTO.setEventTypeCode("EV(\"ITI-55\", \"IHE Transactions\", \"Cross Gateway Patient Discovery\")");
		auditEventDTO.setAuditRequestSource(auditRequestSourceDTO);
		auditEventDTO.setAuditHumanRequestors(auditHumanRequestorDTO);
		auditEventDTO.setAuditRequestDestination(auditRequestDestinationDTO);
		auditEventDTO.setAuditQueryParameters(auditQueryParametersDTO);
		auditEventDTO.setAuditSource(auditSouceDTO);
		addAuditEventEntry(auditEventDTO);
	}

	// create an audit event for an initiating gateway document consumer cross gateway query
	@Transactional
	public void createAuditEventDCXGatewayQuery(String outcome, CommonUser user, String endpointUrl, String repositoryUniqueId, String documentUniqueId, String patientId) throws UnknownHostException, UnsupportedEncodingException{
		AuditRequestSourceDTO auditRequestSourceDTO = AuditUtil.createAuditRequestSource(InetAddress.getLocalHost().getHostAddress() + "/query", // SOAP endpoint URI
				ManagementFactory.getRuntimeMXBean().getName().split("@")[0],
				user.getEmail(), // this is optional
				true, // this is optional
				"EV(110153, DCM, \"Source\")",
				networkAccessPointTypeCodeDao.getByCode("2").getId(),
				InetAddress.getLocalHost().toString());
		ArrayList<AuditHumanRequestorDTO> auditHumanRequestorDTO = AuditUtil.createAuditHumanRequestor(
				user.getSubjectName(),// optional
				user.getUsername(), // optional
				user.getfull_name(), // optional
				true, // optional
				user.getrole(), // optional
				null, // optional
				null);// optional
		AuditRequestDestinationDTO auditRequestDestinationDTO = AuditUtil.createAuditRequestDestination(endpointUrl,
				ManagementFactory.getRuntimeMXBean().getName().split("@")[0], // mandatory
				null, // optional
				true, 
				"EV(110152, DCM, \"Destination\")", 
				networkAccessPointTypeCodeDao.getByCode("2").getId(), 
				endpointUrl);
		AuditPatientDTO auditPatientDTO = AuditUtil.createAuditPatient(participantObjectTypeCodeDao.getByCode("1").getId(), // optional
				participantObjectTypeCodeRoleDao.getByCode("1").getId(), 
				null, // optional 
				"EV(\"ITI-38\", \"IHE Transactions\", and \"Cross Gateway Query\")", 
				null, // optional 
				patientId, // the patient ID in HL7 format
				null, // optional
				null, // 
				null); //
		ArrayList<AuditDocumentDTO> auditDocumentDTO = AuditUtil.createAuditDocument(participantObjectTypeCodeDao.getByCode("2").getId(), 
				participantObjectTypeCodeRoleDao.getByCode("3").getId(), 
				null, // optional 
				"EV(\"ITI-38\", \"IHE Transactions\", and \"Cross Gateway Query\")", // not specialized
				null, // optional 
				documentUniqueId, // document unique id
				null, // optional
				null, // optional
				repositoryUniqueId, // repository unique id
				null); // home community id
		AuditSourceDTO auditSouceDTO = AuditUtil.createAuditSource(null, // optional 
				null, // optional 
				null); // optional
		AuditEventDTO auditEventDTO = new AuditEventDTO();
		auditEventDTO.setEventId("EV(110107, DCM, \"Import\")");
		auditEventDTO.setEventActionCodeId(eventActionCodeDao.getByCode("C").getId());
		auditEventDTO.setEventDateTime(new Date().toString());
		auditEventDTO.setEventOutcomeIndicator(outcome); 
		auditEventDTO.setEventTypeCode("EV(\"ITI-38\", \"IHE Transactions\", and \"Cross Gateway Query\")");
		auditEventDTO.setAuditRequestSource(auditRequestSourceDTO);
		auditEventDTO.setAuditHumanRequestors(auditHumanRequestorDTO);
		auditEventDTO.setAuditRequestDestination(auditRequestDestinationDTO);
		auditEventDTO.setAuditPatient(auditPatientDTO);
		auditEventDTO.setAuditSource(auditSouceDTO);
		auditEventDTO.setAuditDocument(auditDocumentDTO);
		addAuditEventEntry(auditEventDTO);
	}

	// create an audit event for an initiating gateway document consumer cross gateway retrieve
	@Transactional
	public void createAuditEventDCXGatewayRetrieve(String outcome, CommonUser user, String endpointUrl, String repositoryUniqueId, String documentUniqueId, String homeCommunityId, String patientId) throws UnknownHostException, UnsupportedEncodingException{
		AuditRequestSourceDTO auditRequestSourceDTO = AuditUtil.createAuditRequestSource(InetAddress.getLocalHost().getHostAddress() + "/retrieve", // SOAP endpoint URI
				ManagementFactory.getRuntimeMXBean().getName().split("@")[0],
				user.getEmail(), // this is optional
				true, // this is optional
				"EV(110153, DCM, \"Source\")",
				networkAccessPointTypeCodeDao.getByCode("2").getId(),
				InetAddress.getLocalHost().toString());
		ArrayList<AuditHumanRequestorDTO> auditHumanRequestorDTO = AuditUtil.createAuditHumanRequestor(
				user.getSubjectName(),// optional
				user.getUsername(), // optional
				user.getfull_name(), // optional
				true, // optional
				user.getrole(), // optional
				null, // optional
				null);// optional
		AuditRequestDestinationDTO auditRequestDestinationDTO = AuditUtil.createAuditRequestDestination(endpointUrl,
				ManagementFactory.getRuntimeMXBean().getName().split("@")[0], // mandatory
				null, // optional
				true, 
				"EV(110152, DCM, \"Destination\")", 
				networkAccessPointTypeCodeDao.getByCode("2").getId(), 
				endpointUrl);
		AuditPatientDTO auditPatientDTO = AuditUtil.createAuditPatient(participantObjectTypeCodeDao.getByCode("1").getId(), // optional
				participantObjectTypeCodeRoleDao.getByCode("1").getId(), 
				null, // optional 
				"EV(\"ITI-39\", \"IHE Transactions\", and \"Cross Gateway Retrieve\")", // not specialized
				null, // optional 
				patientId, // the patient ID in HL7 format
				null, // optional
				null, // optional
				null); // optional
		ArrayList<AuditDocumentDTO> auditDocumentDTO = AuditUtil.createAuditDocument(participantObjectTypeCodeDao.getByCode("2").getId(), 
				participantObjectTypeCodeRoleDao.getByCode("3").getId(), 
				null, // optional 
				"EV(\"ITI-39\", \"IHE Transactions\", and \"Cross Gateway Retrieve\")", // not specialized
				null, // optional 
				documentUniqueId, // document unique id
				null, // optional
				null, // optional
				repositoryUniqueId, // repository unique id
				homeCommunityId); // home community id
		AuditSourceDTO auditSouceDTO = AuditUtil.createAuditSource(null, // optional 
				null, // optional 
				null); // optional
		AuditEventDTO auditEventDTO = new AuditEventDTO();
		auditEventDTO.setEventId("EV(110107, DCM, \"Import\")");
		auditEventDTO.setEventActionCodeId(eventActionCodeDao.getByCode("C").getId());
		auditEventDTO.setEventDateTime(new Date().toString());
		auditEventDTO.setEventOutcomeIndicator(outcome);
		auditEventDTO.setEventTypeCode("EV(\"ITI-39\", \"IHE Transactions\", and \"Cross Gateway Retrieve\")");
		auditEventDTO.setAuditRequestSource(auditRequestSourceDTO);
		auditEventDTO.setAuditHumanRequestors(auditHumanRequestorDTO);
		auditEventDTO.setAuditRequestDestination(auditRequestDestinationDTO);
		auditEventDTO.setAuditPatient(auditPatientDTO);
		auditEventDTO.setAuditSource(auditSouceDTO);
		auditEventDTO.setAuditDocument(auditDocumentDTO);
		addAuditEventEntry(auditEventDTO);
	}
	
	@Transactional
	public void createPulseAuditEvent(AuditType actionCode, Long id) throws JsonProcessingException, SQLException {
		CommonUser user = UserUtil.getCurrentUser();
		PulseEventActionDTO pulseEventActionDTO = new PulseEventActionDTO();
		String jsonInString = null;
		switch(actionCode) {
		case PD:
			PatientDTO patientDis = patientManager.getPatientById(id);
			patientDis.setEndpointMaps(new ArrayList());
			Patient patientDisAuditObj = DtoToDomainConverter.convert(patientDis);
			jsonInString = mapper.writeValueAsString(patientDisAuditObj);
			break;
		case PC:
			PatientDTO patient = patientManager.getPatientById(id);
			Patient patientAuditObj = DtoToDomainConverter.convert(patient);
			jsonInString = mapper.writeValueAsString(patientAuditObj);
			break;
		case FE:
			AlternateCareFacilityDTO acfDto = acfManager.getById(id);
			AlternateCareFacility acfAuditObj = DtoToDomainConverter.convert(acfDto);
			jsonInString = mapper.writeValueAsString(acfAuditObj);
			break;
		case DV:
			DocumentDTO docDto = documentManager.getDocumentById(id);
			DocumentAudit docAuditObj = DtoToDomainConverter.convertToAuditDoc(docDto);
			jsonInString = mapper.writeValueAsString(docAuditObj);
			break;
		default:
			logger.error("No handler available for action code " + actionCode);
			break;
		}
		
		pulseEventActionDTO.setActionJson(jsonInString);
		pulseEventActionDTO.setUsername(user.getSubjectName());
		pulseEventActionDTO.setPulseEventActionCodeId(pulseEventActionCodeDao.getByCode(actionCode.name()).getId());
		pulseEventActionDao.create(pulseEventActionDTO);
	}

}
