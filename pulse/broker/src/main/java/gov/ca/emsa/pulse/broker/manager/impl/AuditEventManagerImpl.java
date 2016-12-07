package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.dao.AuditEventDAO;
import gov.ca.emsa.pulse.broker.dto.AuditDocumentDTO;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditHumanRequestorDTO;
import gov.ca.emsa.pulse.broker.dto.AuditPatientDTO;
import gov.ca.emsa.pulse.broker.dto.AuditQueryParametersDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestDestinationDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestSourceDTO;
import gov.ca.emsa.pulse.broker.dto.AuditSourceDTO;
import gov.ca.emsa.pulse.broker.manager.AuditEventManager;
import gov.ca.emsa.pulse.service.AuditUtil;
import gov.ca.emsa.pulse.service.UserUtil;

import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditEventManagerImpl implements AuditEventManager{

	@Autowired
	private AuditEventDAO auditEventDao;

	// Initiating gateway audit event
	@Override
	@Transactional
	public AuditEventDTO addAuditEventEntry(AuditEventDTO ae) {
		AuditEventDTO auditEventDTO = new AuditEventDTO();
		auditEventDTO.setEventId(ae.getEventId());
		auditEventDTO.setEventActionCode(ae.getEventActionCode());
		auditEventDTO.setEventDateTime(ae.getEventDateTime());
		auditEventDTO.setEventOutcomeIndicator(ae.getEventOutcomeIndicator());
		auditEventDTO.setEventTypeCode(ae.getEventTypeCode());
		if(ae.getAuditQueryParameters() != null){
			auditEventDTO.setAuditQueryParameters(ae.getAuditQueryParameters());
		}
		if(ae.getAuditRequestDestination() != null){
			auditEventDTO.setAuditRequestDestination(ae.getAuditRequestDestination());
		}
		if(ae.getAuditRequestSource() != null){
			auditEventDTO.setAuditRequestSource(ae.getAuditRequestSource());
		}
		if(ae.getAuditSource() != null){
			auditEventDTO.setAuditSource(ae.getAuditSource());
		}
		if(ae.getAuditHumanRequestors() != null){
			auditEventDTO.setAuditHumanRequestors(ae.getAuditHumanRequestors());
		}
		if(ae.getAuditDocument() != null){
			auditEventDTO.setAuditDocument(ae.getAuditDocument());
		}
		if(ae.getAuditPatient() != null){
			auditEventDTO.setAuditPatient(ae.getAuditPatient());
		}
		AuditEventDTO insertedAuditEventDTO = auditEventDao.createAuditEvent(auditEventDTO);
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
				"EV(110153, DCM, “Source”)",
				"2",
				InetAddress.getLocalHost().toString());
		ArrayList<AuditHumanRequestorDTO> auditHumanRequestorDTO = AuditUtil.createAuditHumanRequestor(user.getFirstName()
				+ " " + user.getLastName(), // the identity of the human that initiated the transaction
				null, // optional
				null, // optional
				true, // optional
				null, // optional
				null, // optional
				null);// optional
		AuditRequestDestinationDTO auditRequestDestinationDTO = AuditUtil.createAuditRequestDestination(endpointUrl,
				null, // optional
				null, // optional
				true, 
				"EV(110152, DCM, “Destination”)", 
				"2", 
				endpointUrl);
		AuditQueryParametersDTO auditQueryParametersDTO = AuditUtil.createAuditQueryParameters(2, 
				24, 
				null, // optional 
				"EV(“ITI-55, “IHE Transactions”, “Cross Gateway Patient Discovery”)", 
				null, // optional 
				null, // optional 
				null, // optional
				AuditUtil.base64EncodeMessage(queryByParameter), // the QueryByParameter segment of the query, base64 encoded.
				homeCommunityId); // The value of “ihe:homeCommunityID” as the value of the attribute type and the value of the homeCommunityID as the value of the attribute value.
		AuditSourceDTO auditSouceDTO = AuditUtil.createAuditSource(null, // optional 
				null, // optional 
				null); // optional
		AuditEventDTO auditEventDTO = new AuditEventDTO();
		auditEventDTO.setEventId("EV(110112, DCM, “Query”)");
		auditEventDTO.setEventActionCode("E");
		auditEventDTO.setEventDateTime(new Date().toString());
		auditEventDTO.setEventOutcomeIndicator(outcome);
		auditEventDTO.setEventTypeCode("EV(“ITI-55”, “IHE Transactions”, “Cross Gateway Patient Discovery”)");
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
				"EV(110153, DCM, “Source”)",
				"2",
				InetAddress.getLocalHost().toString());
		ArrayList<AuditHumanRequestorDTO> auditHumanRequestorDTO = AuditUtil.createAuditHumanRequestor(user.getFirstName()
				+ " " + user.getLastName(), // optional
				null, // optional
				null, // optional
				true, // optional
				null, // optional
				null, // optional
				null);// optional
		AuditRequestDestinationDTO auditRequestDestinationDTO = AuditUtil.createAuditRequestDestination(endpointUrl,
				ManagementFactory.getRuntimeMXBean().getName().split("@")[0], // mandatory
				null, // optional
				true, 
				"EV(110152, DCM, “Destination”)", 
				"2", 
				endpointUrl);
		AuditPatientDTO auditPatientDTO = AuditUtil.createAuditPatient(1, // optional
				1, 
				null, // optional 
				"EV(“ITI-38”, “IHE Transactions”, and “Cross Gateway Query”)", 
				null, // optional 
				patientId, // the patient ID in HL7 format
				null, // optional
				null, // 
				null); //
		AuditDocumentDTO auditDocumentDTO = AuditUtil.createAuditDocument(2, 
				3, 
				null, // optional 
				"EV(“ITI-38”, “IHE Transactions”, and “Cross Gateway Query”)", // not specialized
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
		auditEventDTO.setEventId("EV(110107, DCM, “Import”)");
		auditEventDTO.setEventActionCode("C");
		auditEventDTO.setEventDateTime(new Date().toString());
		auditEventDTO.setEventOutcomeIndicator(outcome); 
		auditEventDTO.setEventTypeCode("EV(“ITI-38”, “IHE Transactions”, and “Cross Gateway Query”)");
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
				"EV(110153, DCM, “Source”)",
				"2",
				InetAddress.getLocalHost().toString());
		ArrayList<AuditHumanRequestorDTO> auditHumanRequestorDTO = AuditUtil.createAuditHumanRequestor(user.getFirstName()
				+ " " + user.getLastName(), // optional
				null, // optional
				null, // optional
				true, // optional
				null, // optional
				null, // optional
				null);// optional
		AuditRequestDestinationDTO auditRequestDestinationDTO = AuditUtil.createAuditRequestDestination(endpointUrl,
				ManagementFactory.getRuntimeMXBean().getName().split("@")[0], // mandatory
				null, // optional
				true, 
				"EV(110152, DCM, “Destination”)", 
				"2", 
				endpointUrl);
		AuditPatientDTO auditPatientDTO = AuditUtil.createAuditPatient(1, // optional
				1, 
				null, // optional 
				"EV(“ITI-39”, “IHE Transactions”, and “Cross Gateway Retrieve”)", // not specialized
				null, // optional 
				patientId, // the patient ID in HL7 format
				null, // optional
				null, // optional
				null); // optional
		AuditDocumentDTO auditDocumentDTO = AuditUtil.createAuditDocument(2, 
				3, 
				null, // optional 
				"EV(“ITI-39”, “IHE Transactions”, and “Cross Gateway Retrieve”)", // not specialized
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
		auditEventDTO.setEventId("EV(110107, DCM, “Import”)");
		auditEventDTO.setEventActionCode("C");
		auditEventDTO.setEventDateTime(new Date().toString());
		auditEventDTO.setEventOutcomeIndicator(outcome);
		auditEventDTO.setEventTypeCode("EV(“ITI-39”, “IHE Transactions”, and “Cross Gateway Retrieve”)");
		auditEventDTO.setAuditRequestSource(auditRequestSourceDTO);
		auditEventDTO.setAuditHumanRequestors(auditHumanRequestorDTO);
		auditEventDTO.setAuditRequestDestination(auditRequestDestinationDTO);
		auditEventDTO.setAuditPatient(auditPatientDTO);
		auditEventDTO.setAuditSource(auditSouceDTO);
		auditEventDTO.setAuditDocument(auditDocumentDTO);
		addAuditEventEntry(auditEventDTO);
	}
}
