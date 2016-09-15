package gov.ca.emsa.pulse.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.JAXBElement;

import gov.ca.emsa.pulse.service.SOAPToJSONService;
import gov.ca.emsa.pulse.common.domain.PatientSearch;

import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAIN201305UV02;
import org.springframework.stereotype.Service;

@Service
public class SOAPToJSONServiceImpl implements SOAPToJSONService {
	
	public PatientSearch convertToPatientSearch(PRPAIN201305UV02 request){
		PRPAMT201306UV02ParameterList requestParameterList = request.getControlActProcess().getQueryByParameter().getValue().getParameterList();
		PatientSearch ps = new PatientSearch();
		List<PRPAMT201306UV02LivingSubjectAdministrativeGender> gender = requestParameterList.getLivingSubjectAdministrativeGender();
		List<PRPAMT201306UV02LivingSubjectBirthTime> dob = requestParameterList.getLivingSubjectBirthTime();
		List<PRPAMT201306UV02LivingSubjectName> name = requestParameterList.getLivingSubjectName();
		if((gender != null && !gender.isEmpty()) && (dob != null && !dob.isEmpty()) && (name != null  && !name.isEmpty())){
			ps.setGender(gender.get(0).getValue().get(0).getCode());
			ps.setDob(dob.get(0).getValue().get(0).getValue());
			List<Serializable> names = name.get(0).getValue().get(0).getContent();
			for(Serializable nameInList: names){
				if(nameInList instanceof JAXBElement<?>){
					if(((JAXBElement<?>) nameInList).getName().getLocalPart().equals("given")){
						ps.setGivenName(((JAXBElement<EnExplicitGiven>) nameInList).getValue().getContent());
					}else if(((JAXBElement<?>) nameInList).getName().getLocalPart().equals("family")){
						ps.setFamilyName(((JAXBElement<EnExplicitFamily>) nameInList).getValue().getContent());
					}
				}
			}
		}
		return ps;
	}

}
