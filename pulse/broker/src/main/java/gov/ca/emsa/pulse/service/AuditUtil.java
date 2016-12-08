package gov.ca.emsa.pulse.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;

import gov.ca.emsa.pulse.broker.audit.AuditHumanRequestor;
import gov.ca.emsa.pulse.broker.dto.AuditDocumentDTO;
import gov.ca.emsa.pulse.broker.dto.AuditHumanRequestorDTO;
import gov.ca.emsa.pulse.broker.dto.AuditPatientDTO;
import gov.ca.emsa.pulse.broker.dto.AuditQueryParametersDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestDestinationDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestSourceDTO;
import gov.ca.emsa.pulse.broker.dto.AuditSourceDTO;

public class AuditUtil {
	
	public static String base64EncodeMessage(String queryByParameters) throws UnsupportedEncodingException{
		byte[] bytesEncoded = Base64.getEncoder().encode(queryByParameters.getBytes());
		String encodedString = new String(bytesEncoded, "UTF-8");
		return encodedString;
	}
	
	public static AuditRequestSourceDTO createAuditRequestSource(String userId, String alternativeUserId, String userName, boolean userIsRequestor,
			String roleIdCode, String networkAccessPointTypeCode, String networkAccessPointId){
		AuditRequestSourceDTO auditRequestSourceDTO = new AuditRequestSourceDTO();
		auditRequestSourceDTO.setUserId(userId);
		auditRequestSourceDTO.setAlternativeUserId(alternativeUserId);
		auditRequestSourceDTO.setUserName(userName);
		auditRequestSourceDTO.setUserIsRequestor(userIsRequestor);
		auditRequestSourceDTO.setRoleIdCode(roleIdCode);
		auditRequestSourceDTO.setNetworkAccessPointTypeCode(networkAccessPointTypeCode);
		auditRequestSourceDTO.setNetworkAccessPointId(networkAccessPointId);
		
		return auditRequestSourceDTO;
	}
	
	public static ArrayList<AuditHumanRequestorDTO> createAuditHumanRequestor(String userId, String alternativeUserId, String userName, boolean userIsRequestor,
			String roleIdCode, String networkAccessPointTypeCode, String networkAccessPointId){
		AuditHumanRequestorDTO auditHumanRequestorDTO = new AuditHumanRequestorDTO();
		auditHumanRequestorDTO.setUserId(userId);
		auditHumanRequestorDTO.setAlternativeUserId(alternativeUserId);
		auditHumanRequestorDTO.setUserName(userName);
		auditHumanRequestorDTO.setUserIsRequestor(userIsRequestor);
		auditHumanRequestorDTO.setRoleIdCode(roleIdCode);
		auditHumanRequestorDTO.setNetworkAccessPointTypeCode(networkAccessPointTypeCode);
		auditHumanRequestorDTO.setNetworkAccessPointId(networkAccessPointId);
		ArrayList<AuditHumanRequestorDTO> humanRequestor = new ArrayList<AuditHumanRequestorDTO>();
		humanRequestor.add(auditHumanRequestorDTO);
		return humanRequestor;
	}
	
	public static AuditRequestDestinationDTO createAuditRequestDestination(String userId, String alternativeUserId, String userName, boolean userIsRequestor,
			String roleIdCode, String networkAccessPointTypeCode, String networkAccessPointId){
		AuditRequestDestinationDTO auditRequestDestinationDTO = new AuditRequestDestinationDTO();
		auditRequestDestinationDTO.setUserId(userId);
		auditRequestDestinationDTO.setAlternativeUserId(alternativeUserId);
		auditRequestDestinationDTO.setUserName(userName);
		auditRequestDestinationDTO.setUserIsRequestor(userIsRequestor);
		auditRequestDestinationDTO.setRoleIdCode(roleIdCode);
		auditRequestDestinationDTO.setNetworkAccessPointTypeCode(networkAccessPointTypeCode);
		auditRequestDestinationDTO.setNetworkAccessPointId(networkAccessPointId);
		
		return auditRequestDestinationDTO;
	}
	
	public static AuditSourceDTO createAuditSource(String auditSourceId, String auditEnterpriseSiteId, String auditSourceTypeCode){
		AuditSourceDTO auditSourceDTO = new AuditSourceDTO();
		auditSourceDTO.setAuditSourceId(auditSourceId);
		auditSourceDTO.setAuditEnterpriseSiteId(auditEnterpriseSiteId);
		auditSourceDTO.setAuditSourceTypeCode(auditSourceTypeCode);
		
		return auditSourceDTO;
	}
	
	public static AuditQueryParametersDTO createAuditQueryParameters(int participantObjectTypeCode, int participantObjectTypeCodeRole,
			String participantObjectDataLifecycle, String participantObjectIdTypeCode, String participantObjectSensitivity, String participantObjectId,
			String participantObjectName, String participantObjectQuery, String participantObjectDetail){
		AuditQueryParametersDTO auditQueryParametersDTO = new AuditQueryParametersDTO();
		auditQueryParametersDTO.setParticipantObjectTypeCode(participantObjectTypeCode);
		auditQueryParametersDTO.setParticipantObjectTypeCodeRole(participantObjectTypeCodeRole);
		auditQueryParametersDTO.setParticipantObjectDataLifecycle(participantObjectDataLifecycle);
		auditQueryParametersDTO.setParticipantObjectIdTypeCode(participantObjectIdTypeCode);
		auditQueryParametersDTO.setParticipantObjectSensitivity(participantObjectSensitivity);
		auditQueryParametersDTO.setParticipantObjectId(participantObjectId);
		auditQueryParametersDTO.setParticipantObjectName(participantObjectName);
		auditQueryParametersDTO.setParticipantObjectQuery(participantObjectQuery);
		auditQueryParametersDTO.setParticipantObjectDetail(participantObjectDetail);
		
		return auditQueryParametersDTO;
	}
	
	public static AuditPatientDTO createAuditPatient(int participantObjectTypeCode, int participantObjectTypeCodeRole,
			String participantObjectDataLifecycle, String participantObjectIdTypeCode, String participantObjectSensitivity, String participantObjectId,
			String participantObjectName, String participantObjectQuery, String participantObjectDetail){
		AuditPatientDTO auditPatientDTO = new AuditPatientDTO();
		auditPatientDTO.setParticipantObjectTypeCode(participantObjectTypeCode);
		auditPatientDTO.setParticipantObjectTypeCodeRole(participantObjectTypeCodeRole);
		auditPatientDTO.setParticipantObjectDataLifecycle(participantObjectDataLifecycle);
		auditPatientDTO.setParticipantObjectIdTypeCode(participantObjectIdTypeCode);
		auditPatientDTO.setParticipantObjectSensitivity(participantObjectSensitivity);
		auditPatientDTO.setParticipantObjectId(participantObjectId);
		auditPatientDTO.setParticipantObjectName(participantObjectName);
		auditPatientDTO.setParticipantObjectQuery(participantObjectQuery);
		auditPatientDTO.setParticipantObjectDetail(participantObjectDetail);
		
		return auditPatientDTO;
	}
	
	public static ArrayList<AuditDocumentDTO> createAuditDocument(int participantObjectTypeCode, int participantObjectTypeCodeRole,
			String participantObjectDataLifecycle, String participantObjectIdTypeCode, String participantObjectSensitivity, String participantObjectId,
			String participantObjectName, String participantObjectQuery, String participantObjectDetail, String participantObjectDetail2){
		AuditDocumentDTO auditDocumentDTO = new AuditDocumentDTO();
		auditDocumentDTO.setParticipantObjectTypeCode(participantObjectTypeCode);
		auditDocumentDTO.setParticipantObjectTypeCodeRole(participantObjectTypeCodeRole);
		auditDocumentDTO.setParticipantObjectDataLifecycle(participantObjectDataLifecycle);
		auditDocumentDTO.setParticipantObjectIdTypeCode(participantObjectIdTypeCode);
		auditDocumentDTO.setParticipantObjectSensitivity(participantObjectSensitivity);
		auditDocumentDTO.setParticipantObjectId(participantObjectId);
		auditDocumentDTO.setParticipantObjectName(participantObjectName);
		auditDocumentDTO.setParticipantObjectQuery(participantObjectQuery);
		auditDocumentDTO.setParticipantObjectDetail(participantObjectDetail);
		auditDocumentDTO.setParticipantObjectDetail2(participantObjectDetail2);
		ArrayList<AuditDocumentDTO> auditDocumentDTOs = new ArrayList<AuditDocumentDTO>();
		auditDocumentDTOs.add(auditDocumentDTO);
		
		return auditDocumentDTOs;
	}

}
