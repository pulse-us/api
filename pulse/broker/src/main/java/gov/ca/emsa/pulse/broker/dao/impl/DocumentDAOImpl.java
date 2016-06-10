package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.broker.dao.DocumentDAO;
import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.entity.DocumentEntity;
import gov.ca.emsa.pulse.broker.entity.PatientEntity;

@Repository
public class DocumentDAOImpl extends BaseDAOImpl implements DocumentDAO {
	private static final Logger logger = LogManager.getLogger(DocumentDAOImpl.class);
	
	@Override
	public DocumentDTO create(DocumentDTO dto) {
		DocumentEntity doc = new DocumentEntity();
		doc.setName(dto.getName());
		doc.setFormat(dto.getFormat());
		doc.setContents(dto.getContents());
		doc.setLastReadDate(new Date());
		if(dto.getPatient() != null && dto.getPatient().getId() != null) {
			doc.setPatientId(dto.getPatient().getId());
		}
		
		entityManager.persist(doc);
		entityManager.flush();
		return new DocumentDTO(doc);
	}

	@Override
	@Transactional
	public DocumentDTO update(DocumentDTO dto) {
		DocumentEntity doc = this.getEntityById(dto.getId());		
		doc.setName(dto.getName());
		doc.setFormat(dto.getFormat());
		doc.setContents(dto.getContents());
		doc.setLastReadDate(new Date());
		if(dto.getPatient() != null && dto.getPatient().getId() != null) {
			doc.setPatientId(dto.getPatient().getId());
		}
		
		doc = entityManager.merge(doc);
		entityManager.flush();
		return new DocumentDTO(doc);
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
	public List<DocumentDTO> getByPatientId(Long patientId) {
		List<DocumentEntity> documents = getEntityByPatientId(patientId);
		List<DocumentDTO> results = new ArrayList<DocumentDTO>(documents.size());
		
		for(DocumentEntity dEntity : documents) {
			DocumentDTO docDto = new DocumentDTO(dEntity);
			results.add(docDto);
		}
		return results;
	}
	
	public void deleteItemsOlderThan(Date oldestDate) {			
		Query query = entityManager.createQuery( "DELETE from DocumentEntity doc "
				+ " WHERE doc.lastReadDate <= :cacheDate");
		
		query.setParameter("cacheDate", oldestDate);
		int deletedCount = query.executeUpdate();
		logger.info("Deleted " + deletedCount + " documents from the cache.");
	}
	
	private List<DocumentEntity> findAllEntities() {
		Query query = entityManager.createQuery("from DocumentEntity");
		return query.getResultList();
	}
	
	private DocumentEntity getEntityById(Long id) {
		DocumentEntity entity = null;
		
		Query query = entityManager.createQuery( "SELECT doc from DocumentEntity doc "
				+ "LEFT OUTER JOIN FETCH doc.patient "
				+ "where doc.id = :entityid) ", 
				DocumentEntity.class );
		
		query.setParameter("entityid", id);
		List<DocumentEntity> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}
		
		return entity;
	}
	
	private List<DocumentEntity> getEntityByPatientId(Long patientId) {		
		Query query = entityManager.createQuery( "SELECT doc from DocumentEntity doc "
				+ "LEFT OUTER JOIN FETCH doc.patient "
				+ "where doc.patientId = :patientId", 
				DocumentEntity.class );
		
		query.setParameter("patientId", patientId);
		return query.getResultList();
	}
}
