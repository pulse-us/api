package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.AuditEventDAO;
import gov.ca.emsa.pulse.broker.dao.AuditHumanRequestorDAO;
import gov.ca.emsa.pulse.broker.dao.AuditPatientDAO;
import gov.ca.emsa.pulse.broker.dao.AuditQueryParametersDAO;
import gov.ca.emsa.pulse.broker.dao.AuditRequestDestinationDAO;
import gov.ca.emsa.pulse.broker.dao.AuditRequestSourceDAO;
import gov.ca.emsa.pulse.broker.dao.AuditSourceDAO;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditHumanRequestorDTO;
import gov.ca.emsa.pulse.broker.dto.AuditPatientDTO;
import gov.ca.emsa.pulse.broker.dto.AuditQueryParametersDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestDestinationDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestSourceDTO;
import gov.ca.emsa.pulse.broker.dto.AuditSourceDTO;
import gov.ca.emsa.pulse.broker.entity.AuditEventEntity;
import gov.ca.emsa.pulse.broker.entity.AuditHumanRequestorEntity;
import gov.ca.emsa.pulse.broker.entity.AuditPatientEntity;
import gov.ca.emsa.pulse.broker.entity.AuditQueryParametersEntity;
import gov.ca.emsa.pulse.broker.entity.AuditRequestDestinationEntity;
import gov.ca.emsa.pulse.broker.entity.AuditRequestSourceEntity;
import gov.ca.emsa.pulse.broker.entity.AuditSourceEntity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AuditEventDAOImpl extends BaseDAOImpl implements AuditEventDAO {
	private static final Logger logger = LogManager.getLogger(AuditEventDAOImpl.class);
	
	@Autowired AuditHumanRequestorDAO auditHumanRequestorDAO;
	@Autowired AuditPatientDAO auditPatientDAO;
	@Autowired AuditQueryParametersDAO auditQueryParametersDAO;
	@Autowired AuditRequestDestinationDAO auditRequestDestinationDAO;
	@Autowired AuditRequestSourceDAO auditRequestSourceDAO;
	@Autowired AuditSourceDAO auditSourceDAO;
	
	
	@Override
	public AuditEventDTO createAuditEvent(AuditEventDTO dto) {
		AuditEventEntity audit = new AuditEventEntity();
		audit.setEventId(dto.getEventId());
		audit.setEventActionCode(dto.getEventActionCode());
		audit.setEventDateTime(dto.getEventDateTime());
		audit.setEventOutcomeIndicator(dto.getEventOutcomeIndicator());
		audit.setEventTypeCode(dto.getEventTypeCode());
		
		AuditRequestSourceEntity toInsertRequestSourceEntity = new AuditRequestSourceEntity();
		AuditRequestSourceDTO insertedRequestSource = auditRequestSourceDAO.createAuditRequestSource(dto.getAuditRequestSource());
		toInsertRequestSourceEntity.setAlternativeUserId(insertedRequestSource.getAlternativeUserId());
		toInsertRequestSourceEntity.setId(insertedRequestSource.getId());
		toInsertRequestSourceEntity.setNetworkAccessPointId(insertedRequestSource.getNetworkAccessPointId());
		toInsertRequestSourceEntity.setNetworkAccessPointTypeCode(insertedRequestSource.getNetworkAccessPointTypeCode());
		toInsertRequestSourceEntity.setRoleIdCode(insertedRequestSource.getRoleIdCode());
		toInsertRequestSourceEntity.setUserId(insertedRequestSource.getUserId());
		toInsertRequestSourceEntity.setUserIsRequestor(insertedRequestSource.isUserIsRequestor());
		toInsertRequestSourceEntity.setUserName(insertedRequestSource.getUserName());
		audit.setAuditRequestSource(toInsertRequestSourceEntity);
		
		AuditRequestDestinationEntity toInsertAuditRequestDestinationEntity = new AuditRequestDestinationEntity();
		AuditRequestDestinationDTO insertedRequestDestination = auditRequestDestinationDAO.createAuditRequestDestination(dto.getAuditRequestDestination());
		toInsertAuditRequestDestinationEntity.setAlternativeUserId(insertedRequestDestination.getAlternativeUserId());
		toInsertAuditRequestDestinationEntity.setId(insertedRequestDestination.getId());
		toInsertAuditRequestDestinationEntity.setNetworkAccessPointId(insertedRequestDestination.getNetworkAccessPointId());
		toInsertAuditRequestDestinationEntity.setNetworkAccessPointTypeCode(insertedRequestDestination.getNetworkAccessPointTypeCode());
		toInsertAuditRequestDestinationEntity.setRoleIdCode(insertedRequestDestination.getRoleIdCode());
		toInsertAuditRequestDestinationEntity.setUserId(insertedRequestDestination.getUserId());
		toInsertAuditRequestDestinationEntity.setUserIsRequestor(insertedRequestDestination.isUserIsRequestor());
		toInsertAuditRequestDestinationEntity.setUserName(insertedRequestDestination.getUserName());
		audit.setAuditRequestDestination(toInsertAuditRequestDestinationEntity);
		
		AuditSourceEntity toInsertAuditSourceEntity = new AuditSourceEntity();
		AuditSourceDTO toInsertAuditSourceDTO = auditSourceDAO.createAuditSource(dto.getAuditSource());
		toInsertAuditSourceEntity.setId(toInsertAuditSourceDTO.getId());
		toInsertAuditSourceEntity.setAuditEnterpriseSiteId(toInsertAuditSourceDTO.getAuditEnterpriseSiteId());
		toInsertAuditSourceEntity.setAuditSourceId(toInsertAuditSourceDTO.getAuditSourceId());
		toInsertAuditSourceEntity.setAuditSourceTypeCode(toInsertAuditSourceDTO.getAuditSourceTypeCode());
		audit.setAuditSource(toInsertAuditSourceEntity);

		AuditQueryParametersEntity toInsertAuditQueryParameterEntity = new AuditQueryParametersEntity();
		AuditQueryParametersDTO toInsertAuditQueryParameterDTO = auditQueryParametersDAO.createAuditQueryParameters(dto.getAuditQueryParameters());
		toInsertAuditQueryParameterEntity.setId(toInsertAuditQueryParameterDTO.getId());
		toInsertAuditQueryParameterEntity.setParticipantObjectDataLifecycle(toInsertAuditQueryParameterDTO.getParticipantObjectDataLifecycle());
		toInsertAuditQueryParameterEntity.setParticipantObjectTypeCode(toInsertAuditQueryParameterDTO.getParticipantObjectTypeCode());
		toInsertAuditQueryParameterEntity.setParticipantObjectTypeCodeRole(toInsertAuditQueryParameterDTO.getParticipantObjectTypeCodeRole());
		toInsertAuditQueryParameterEntity.setParticipantObjectIdTypeCode(toInsertAuditQueryParameterDTO.getParticipantObjectIdTypeCode());
		toInsertAuditQueryParameterEntity.setParticipantObjectSensitivity(toInsertAuditQueryParameterDTO.getParticipantObjectSensitivity());
		toInsertAuditQueryParameterEntity.setParticipantObjectId(toInsertAuditQueryParameterDTO.getParticipantObjectId());
		toInsertAuditQueryParameterEntity.setParticipantObjectName(toInsertAuditQueryParameterDTO.getParticipantObjectName());
		toInsertAuditQueryParameterEntity.setParticipantObjectQuery(toInsertAuditQueryParameterDTO.getParticipantObjectQuery());
		toInsertAuditQueryParameterEntity.setParticipantObjectDetail(toInsertAuditQueryParameterDTO.getParticipantObjectDetail());
		audit.setAuditQueryParameters(toInsertAuditQueryParameterEntity);
		
		
		
		if(dto.getAuditPatient() != null){
			AuditPatientEntity auditPatientEntity = new AuditPatientEntity();
			AuditPatientDTO auditPatientDTO = auditPatientDAO.createAuditPatient(dto.getAuditPatient());
			auditPatientEntity.setId(auditPatientDTO.getId());
			auditPatientEntity.setParticipantObjectDataLifecycle(auditPatientDTO.getParticipantObjectDataLifecycle());
			auditPatientEntity.setParticipantObjectTypeCode(auditPatientDTO.getParticipantObjectTypeCode());
			auditPatientEntity.setParticipantObjectTypeCodeRole(auditPatientDTO.getParticipantObjectTypeCodeRole());
			auditPatientEntity.setParticipantObjectIdTypeCode(auditPatientDTO.getParticipantObjectIdTypeCode());
			auditPatientEntity.setParticipantObjectSensitivity(auditPatientDTO.getParticipantObjectSensitivity());
			auditPatientEntity.setParticipantObjectId(auditPatientDTO.getParticipantObjectId());
			auditPatientEntity.setParticipantObjectName(auditPatientDTO.getParticipantObjectName());
			auditPatientEntity.setParticipantObjectQuery(auditPatientDTO.getParticipantObjectQuery());
			auditPatientEntity.setParticipantObjectDetail(auditPatientDTO.getParticipantObjectDetail());
			audit.setAuditPatient(auditPatientEntity);
		}
		
		entityManager.persist(audit);
		entityManager.flush();
		
		ArrayList<AuditHumanRequestorEntity> humanRequestorEntities = new ArrayList<AuditHumanRequestorEntity>();
		if(dto.getAuditHumanRequestors() != null){
			for(AuditHumanRequestorDTO humanRequestorDTO : dto.getAuditHumanRequestors()){
				AuditHumanRequestorEntity toInsertAuditHumanRequestorEntity = new AuditHumanRequestorEntity();
				toInsertAuditHumanRequestorEntity.setAlternativeUserId(humanRequestorDTO.getAlternativeUserId());
				toInsertAuditHumanRequestorEntity.setAuditEventId(audit.getId());
				toInsertAuditHumanRequestorEntity.setId(humanRequestorDTO.getId());
				toInsertAuditHumanRequestorEntity.setNetworkAccessPointId(humanRequestorDTO.getNetworkAccessPointId());
				toInsertAuditHumanRequestorEntity.setNetworkAccessPointTypeCode(humanRequestorDTO.getNetworkAccessPointTypeCode());
				toInsertAuditHumanRequestorEntity.setRoleIdCode(humanRequestorDTO.getRoleIdCode());
				toInsertAuditHumanRequestorEntity.setUserId(humanRequestorDTO.getUserId());
				toInsertAuditHumanRequestorEntity.setUserIsRequestor(humanRequestorDTO.isUserIsRequestor());
				toInsertAuditHumanRequestorEntity.setUserName(humanRequestorDTO.getUserName());
				AuditHumanRequestorDTO toInsertAuditHumanRequestorDTO = auditHumanRequestorDAO.createAuditHumanRequestor(new AuditHumanRequestorDTO(toInsertAuditHumanRequestorEntity));
				humanRequestorEntities.add(toInsertAuditHumanRequestorEntity);
			}
		}
		audit.setAuditHumanRequestor(humanRequestorEntities);
		
		return new AuditEventDTO(audit);
	}
	
	@Override
	public AuditEventDTO getAuditEventById(Long id) {
		
		AuditEventDTO dto = null;
		AuditEventEntity pe = this.getAuditEventEntityById(id);
		
		if (pe != null){
			dto = new AuditEventDTO(pe); 
		}
		return dto;
	}
	
	private AuditEventEntity getAuditEventEntityById(Long id) {
		AuditEventEntity entity = null;
		
		Query query = entityManager.createQuery( "from AuditEventEntity aud"
				+ "LEFT OUTER JOIN FETCH aud.auditRequestSource "
				+ "LEFT OUTER JOIN FETCH aud.auditRequestDestination "
				+ "LEFT OUTER JOIN FETCH aud.auditSource "
				+ "LEFT OUTER JOIN FETCH aud.auditQueryParameters "
				+ "LEFT OUTER JOIN FETCH aud.auditHumanRequestor "
				+ "where aud.id = :entityid) ", 
				AuditEventEntity.class );
		
		query.setParameter("entityid", id);
		List<AuditEventEntity> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}
		
		return entity;
	}
	
	@Override
	public List<AuditEventDTO> findAllAuditEvents() {
		ArrayList<AuditEventDTO> dto = null;
		
		List<AuditEventEntity> pe = this.getAllAuditEventEntities();
		
		List<AuditEventDTO> dtos = new ArrayList<AuditEventDTO>();
		for(AuditEventEntity eventEnt : pe){
			dtos.add(new AuditEventDTO(eventEnt));
		}
		
		return dtos;
	}
	
	private List<AuditEventEntity> getAllAuditEventEntities() {
		
		Query query = entityManager.createQuery( "from AuditEventEntity", 
				AuditEventEntity.class );
		
		return query.getResultList();
	}
	
}
