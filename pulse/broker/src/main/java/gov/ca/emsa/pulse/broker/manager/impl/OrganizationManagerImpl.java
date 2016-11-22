package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.broker.dao.OrganizationDAO;
import gov.ca.emsa.pulse.broker.dao.PatientDiscoveryQueryStatisticsDAO;
import gov.ca.emsa.pulse.broker.dao.QueryStatusDAO;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.entity.PatientDiscoveryRequestStatisticsEntity;
import gov.ca.emsa.pulse.broker.manager.OrganizationManager;
import gov.ca.emsa.pulse.common.domain.Organization;
import gov.ca.emsa.pulse.common.domain.stats.OrganizationStatistics;
import gov.ca.emsa.pulse.common.domain.stats.RequestStatistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationManagerImpl implements OrganizationManager {
	
	@Autowired private OrganizationDAO organizationDAO;
	@Autowired private PatientDiscoveryQueryStatisticsDAO statsDao;
	
	public OrganizationDTO getById(Long id) {
		return organizationDAO.findById(id);
	}
	
	@Transactional
	public void updateOrganizations(ArrayList<Organization> orgs){
		List<OrganizationDTO> currentOrgs = getAll();
		HashMap<Long,OrganizationDTO> currentOrgsHash = new HashMap<Long,OrganizationDTO>();
		// create a hash of {Organization Id, OrganizationDTO}
		for(OrganizationDTO currentOrg : currentOrgs){
			currentOrgsHash.put(currentOrg.getOrganizationId(), currentOrg);
		}
		List<Long> currentOrgIds = new ArrayList<Long>();
		List<Long> updatedOrgIds = new ArrayList<Long>();
		// get list of id's that relate to org's already in the database
		for(OrganizationDTO orgdto : currentOrgs){
			currentOrgIds.add(orgdto.getOrganizationId());
		}
		// get id's of the incoming org's in the directory services
		for(Organization org: orgs){
			updatedOrgIds.add(org.getOrganizationId());
		}
		
		Set<Long> nonDupUpdatedOrgIds = new HashSet<Long>();
		//create set of updates org ids
		nonDupUpdatedOrgIds.addAll(updatedOrgIds);
		// if the set id size doesnt equal the organization ids size then that 
		// means the organization ids has a duplicate
		if(nonDupUpdatedOrgIds.size() < updatedOrgIds.size()){
			// find the duplicate
			for(int i=0; i<updatedOrgIds.size()-1; i++){
				for(int j=0; j<updatedOrgIds.size()-1; j++){
					// if the duplicate is already in the database then delete it
					if(updatedOrgIds.get(i).equals(updatedOrgIds.get(j)) && i!=j
							&& currentOrgsHash.get(updatedOrgIds.get(i)) != null){
						organizationDAO.delete(currentOrgsHash.get(updatedOrgIds.get(i)));
						Iterator<Organization> iter = orgs.iterator();
						while(iter.hasNext()){
							Organization org = iter.next();
							if(org.getOrganizationId().equals(updatedOrgIds.get(i))){
								iter.remove();
							}
						}
						currentOrgsHash.remove(updatedOrgIds.get(i));
						nonDupUpdatedOrgIds.remove(updatedOrgIds.get(i));
						currentOrgIds.remove(updatedOrgIds.get(i));
						updatedOrgIds.clear();
						updatedOrgIds.addAll(nonDupUpdatedOrgIds);
						// if the duplicate isnt in the database yet just remove it from the org's to insert
					}else if(updatedOrgIds.get(i).equals(updatedOrgIds.get(j)) && i!=j
							&& currentOrgsHash.get(updatedOrgIds.get(i)) == null){
						Iterator<Organization> iter = orgs.iterator();
						while(iter.hasNext()){
							Organization org = iter.next();
							if(org.getOrganizationId().equals(updatedOrgIds.get(i))){
								iter.remove();
							}
						}
						currentOrgsHash.remove(updatedOrgIds.get(i));
						nonDupUpdatedOrgIds.remove(updatedOrgIds.get(i));
						currentOrgIds.remove(updatedOrgIds.get(i));
						updatedOrgIds.clear();
						updatedOrgIds.addAll(nonDupUpdatedOrgIds);
					}
				}
			}
		}
		// for every org thats in the database 
		for(Long currentId : currentOrgIds){
			if(!updatedOrgIds.contains(currentId)){
				organizationDAO.delete(currentOrgsHash.get(currentId));
			}
		}
		for(Organization org: orgs){
			OrganizationDTO orgdto = new OrganizationDTO();
			orgdto.setName(org.getName());
			orgdto.setOrganizationId(org.getOrganizationId());
			orgdto.setActive(org.isActive());
			orgdto.setAdapter(org.getAdapter());
			orgdto.setIpAddress(org.getIpAddress());
			if(org.getUsername() != null){
				orgdto.setUsername(org.getUsername());
			}
			if(org.getPassword() != null){
				orgdto.setPassword(org.getPassword());
			}
			if(org.getCertificationKey() != null){
				orgdto.setCertificationKey(org.getCertificationKey());
			}
			if(org.getEndpointUrl() != null){
				orgdto.setEndpointUrl(org.getEndpointUrl());
			}
			orgdto.setCreationDate(new Date());
			orgdto.setLastModifiedDate(new Date());
			try{
				organizationDAO.create(orgdto);
			}catch(EntityExistsException e){
				organizationDAO.update(orgdto);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<OrganizationDTO> getAll() {
		List<OrganizationDTO> allOrganizations = organizationDAO.findAll();
		return allOrganizations;
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrganizationStatistics> getPatientDiscoveryRequestStatistics(Date startDate, Date endDate) {
		List<PatientDiscoveryRequestStatisticsEntity> stats = statsDao.getStatistics(startDate, endDate);
		List<OrganizationStatistics> results = new ArrayList<OrganizationStatistics>();
		if(stats != null) {
			for(PatientDiscoveryRequestStatisticsEntity stat : stats) {
				OrganizationStatistics result = new OrganizationStatistics();
				result.setCalculationStart(startDate);
				result.setCalculationEnd(endDate);
				//don't have a filter param for calculating based on the last N events yet
				Organization org = new Organization();
				org.setId(stat.getLocationId());
				org.setName(stat.getLocationName());
				org.setActive(stat.getLocationStatus());
				org.setAdapter(stat.getLocationType());
				result.setOrg(org);
				RequestStatistics orgStat = new RequestStatistics();
				orgStat.setRequestCount(stat.getTotalRequestCount());
				orgStat.setRequestAvgCompletionSeconds(stat.getTotalRequestAverageSeconds() != null ? stat.getTotalRequestAverageSeconds().longValue() : null);
				orgStat.setRequestSuccessCount(stat.getSuccessfulRequestCount());
				orgStat.setRequestSuccessAvgCompletionSeconds(stat.getSuccessfulRequestAverageSeconds() != null ? stat.getSuccessfulRequestAverageSeconds().longValue() : null);
				orgStat.setRequestFailureCount(stat.getFailedRequestCount());
				orgStat.setRequestFailureAvgCompletionSeconds(stat.getFailedRequestAverageSeconds() != null ? stat.getFailedRequestAverageSeconds().longValue() : null);
				orgStat.setRequestCancelledCount(stat.getCancelledRequestCount());
				orgStat.setRequestCancelledAvgCompletionSeconds(stat.getCancelledRequestAverageSeconds() != null ? stat.getCancelledRequestAverageSeconds().longValue() : null);
				result.setPatientDiscoveryStats(orgStat);
				results.add(result);
			}
		}
		return results;
	}
}
