package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.NetworkAccessPointTypeCodeDAO;
import gov.ca.emsa.pulse.broker.dto.NetworkAccessPointTypeCodeDTO;
import gov.ca.emsa.pulse.broker.entity.NetworkAccessPointTypeCodeEntity;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class NetworkAccessPointTypeCodeDAOImpl extends BaseDAOImpl implements NetworkAccessPointTypeCodeDAO {
	private static final Logger logger = LogManager.getLogger(NetworkAccessPointTypeCodeDAOImpl.class);

	@Override
	public NetworkAccessPointTypeCodeDTO create(NetworkAccessPointTypeCodeDTO dto) {
		NetworkAccessPointTypeCodeEntity patient = new NetworkAccessPointTypeCodeEntity();
		patient.setCode(dto.getCode());
		patient.setDescription(dto.getDescription());
		patient.setId(dto.getId());
		
		entityManager.persist(patient);
		entityManager.flush();
		return new NetworkAccessPointTypeCodeDTO(patient);
	}

	@Override
	public NetworkAccessPointTypeCodeDTO update(NetworkAccessPointTypeCodeDTO dto) {
		NetworkAccessPointTypeCodeEntity patient = this.getEntityById(dto.getId());
		if(dto.getCode() != null){
			patient.setCode(dto.getCode());
		}
		if(dto.getDescription() != null){
			patient.setDescription(dto.getDescription());
		}

		patient = entityManager.merge(patient);
		entityManager.flush();
		return new NetworkAccessPointTypeCodeDTO(patient);
	}

	@Override
	public void delete(Long id) {
		NetworkAccessPointTypeCodeEntity toDelete = getEntityById(id);
		if(toDelete != null) {
			try {
				entityManager.remove(toDelete);
				entityManager.flush();
			} catch(Exception ex) {
				logger.error("Could not delete patient name. Was it removed before the delete method was called?", ex);
			}
		}
	}


	@Override
	public NetworkAccessPointTypeCodeDTO getById(Long id) {
		NetworkAccessPointTypeCodeDTO dto = null;
		NetworkAccessPointTypeCodeEntity pe = this.getEntityById(id);

		if (pe != null){
			dto = new NetworkAccessPointTypeCodeDTO(pe); 
		}
		return dto;
	}
	
	@Override
	public NetworkAccessPointTypeCodeDTO getByCode(String code) {
		NetworkAccessPointTypeCodeDTO dto = null;
		NetworkAccessPointTypeCodeEntity pe = this.getEntityByCode(code);

		if (pe != null){
			dto = new NetworkAccessPointTypeCodeDTO(pe); 
		}
		return dto;
	}
	
	private NetworkAccessPointTypeCodeEntity getEntityByCode(String code) {
		NetworkAccessPointTypeCodeEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct typeCode from NetworkAccessPointTypeCodeEntity typeCode "
				+ "where typeCode.code = :entityid) ", 
				NetworkAccessPointTypeCodeEntity.class );

		query.setParameter("entityid", code);
		List<NetworkAccessPointTypeCodeEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}

	private NetworkAccessPointTypeCodeEntity getEntityById(Long id) {
		NetworkAccessPointTypeCodeEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct typeCode from NetworkAccessPointTypeCodeEntity typeCode "
				+ "where typeCode.id = :entityid) ", 
				NetworkAccessPointTypeCodeEntity.class );

		query.setParameter("entityid", id);
		List<NetworkAccessPointTypeCodeEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}
}
