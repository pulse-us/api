package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.DocumentDAO;
import gov.ca.emsa.pulse.broker.dao.QueryStatusDAO;
import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.entity.DocumentEntity;
import gov.ca.emsa.pulse.broker.entity.QueryEndpointStatusEntity;
import gov.ca.emsa.pulse.common.domain.QueryEndpointStatus;
import gov.ca.emsa.pulse.common.domain.QueryStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DocumentDAOImpl extends BaseDAOImpl implements DocumentDAO {
	private static final Logger logger = LogManager.getLogger(DocumentDAOImpl.class);
	@Autowired QueryStatusDAO statusDao;
	
	@Override
	public DocumentDTO create(DocumentDTO dto) {
		DocumentEntity doc = new DocumentEntity();
		doc.setStatus(null);
		doc.setName(dto.getName());
		doc.setFormat(dto.getFormat());
		doc.setClassName(dto.getClassName());
		doc.setConfidentiality(dto.getConfidentiality());
		doc.setCreationTime(dto.getCreationTime());
		doc.setDescription(dto.getDescription());
		doc.setDocumentUniqueId(dto.getDocumentUniqueId());
		doc.setHomeCommunityId(dto.getHomeCommunityId());
		doc.setRepositoryUniqueId(dto.getRepositoryUniqueId());
		doc.setSize(dto.getSize());
		doc.setContents(dto.getContents());
		doc.setLastReadDate(new Date());
		doc.setPatientEndpointMapId(dto.getPatientEndpointMapId());
		
		entityManager.persist(doc);
		entityManager.flush();
		entityManager.clear();
		return getById(doc.getId());
	}

	@Override
	@Transactional
	public DocumentDTO update(DocumentDTO toUpdate) {
		logger.info("Trying to update document " + toUpdate.getId() + " to status " + toUpdate.getStatus());
		DocumentEntity existingDocument = this.getEntityById(toUpdate.getId());	
		logger.info("Found document: " + existingDocument);
		
		if(existingDocument.getStatus() == null
			||
			(toUpdate.getStatus() != null && 
			toUpdate.getStatus() == QueryEndpointStatus.Closed) 
			||
			(existingDocument.getStatus() != null && 
			existingDocument.getStatus().getStatus() != QueryEndpointStatus.Cancelled && 
			existingDocument.getStatus().getStatus() != QueryEndpointStatus.Closed)) {
				//always change the status if we are moving to Closed.
				//aside from that, don't do any update if the document is currently Cancelled or Closed.
				QueryEndpointStatusEntity newStatus = 
						statusDao.getQueryEndpointStatusByName(toUpdate.getStatus().name());
				existingDocument.setStatusId(newStatus == null ? null : newStatus.getId());
				existingDocument.setName(toUpdate.getName());
				existingDocument.setFormat(toUpdate.getFormat());
				existingDocument.setConfidentiality(toUpdate.getConfidentiality());
				existingDocument.setCreationTime(toUpdate.getCreationTime());
				existingDocument.setDescription(toUpdate.getDescription());
				existingDocument.setDocumentUniqueId(toUpdate.getDocumentUniqueId());
				existingDocument.setHomeCommunityId(toUpdate.getHomeCommunityId());
				existingDocument.setRepositoryUniqueId(toUpdate.getRepositoryUniqueId());
				existingDocument.setSize(toUpdate.getSize());
				existingDocument.setContents(toUpdate.getContents());
				existingDocument.setLastReadDate(new Date());
				existingDocument.setPatientEndpointMapId(toUpdate.getPatientEndpointMapId());
				
				existingDocument = entityManager.merge(existingDocument);
				entityManager.flush();
		} 
		
		return new DocumentDTO(existingDocument);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		DocumentEntity toDelete = getEntityById(id);
		entityManager.remove(toDelete);
		entityManager.flush();
	}

	@Override
	public List<DocumentDTO> findAll() {
		List<DocumentEntity> result = this.findAllEntities();
		List<DocumentDTO> dtos = new ArrayList<DocumentDTO>(result.size());
		for(DocumentEntity entity : result) {
			dtos.add(new DocumentDTO(entity));
		}
		return dtos;
	}
	
	@Override
	public DocumentDTO getById(Long id) {
		
		DocumentDTO dto = null;
		DocumentEntity pe = this.getEntityById(id);
		
		if (pe != null){
			dto = new DocumentDTO(pe); 
		}
		return dto;
	}

	@Override
	public List<DocumentDTO> getDocumentsWithStatusForPatient(Long patientId, List<QueryEndpointStatus> statuses) {
		List<DocumentEntity> documents = getEntitiesWithStatusForPatient(patientId, statuses);
		List<DocumentDTO> results = new ArrayList<DocumentDTO>(documents.size());
		
		for(DocumentEntity dEntity : documents) {
			DocumentDTO docDto = new DocumentDTO(dEntity);
			results.add(docDto);
		}
		return results;
	}
	
	private List<DocumentEntity> findAllEntities() {
		Query query = entityManager.createQuery("SELECT doc from DocumentEntity doc "
				+ "LEFT JOIN FETCH doc.status docStatus "
				+ "WHERE docStatus.status != 'Closed'");
		return query.getResultList();
	}
	
	private DocumentEntity getEntityById(Long id) {
		DocumentEntity entity = null;
		
		Query query = entityManager.createQuery( "SELECT doc from DocumentEntity doc "
				+ "LEFT JOIN FETCH doc.patientEndpointMap "
				+ "LEFT JOIN FETCH doc.status "
				+ "where doc.id = :entityid) ", 
				DocumentEntity.class );
		
		query.setParameter("entityid", id);
		List<DocumentEntity> result = query.getResultList();
		if(result != null && result.size() > 0) {
			entity = result.get(0);
		}
		
		return entity;
	}
	
	private List<DocumentEntity> getEntitiesWithStatusForPatient(Long patientId, List<QueryEndpointStatus> statuses) {		
		Query query = entityManager.createQuery( "SELECT doc "
				+ "FROM DocumentEntity doc, PatientEndpointMapEntity patientEndpointMap "
				+ "LEFT JOIN FETCH doc.status docStatus "
				+ "WHERE (doc.statusId IS NULL "
					+ " OR "
					+ "docStatus.status IN (:statuses)) "
				+ "AND doc.patientEndpointMapId = patientEndpointMap.id "
				+ "AND patientEndpointMap.patientId = :patientId", 
				DocumentEntity.class );
		
		query.setParameter("patientId", patientId);
		query.setParameter("statuses", statuses);
		return query.getResultList();
	}
}
