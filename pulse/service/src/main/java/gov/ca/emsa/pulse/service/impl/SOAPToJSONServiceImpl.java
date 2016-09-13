package gov.ca.emsa.pulse.service.impl;

import java.util.List;
import gov.ca.emsa.pulse.service.SOAPToJSONService;

import gov.ca.emsa.pulse.common.domain.PatientSearch;

import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.springframework.stereotype.Service;
import org.hl7.v3.PRPAIN201305UV02;

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
			ps.setGivenName(name.get(0).getValue().get(0).toString());
			ps.setFamilyName(name.get(0).getValue().get(1).toString());
		}
		return ps;
	}

}
