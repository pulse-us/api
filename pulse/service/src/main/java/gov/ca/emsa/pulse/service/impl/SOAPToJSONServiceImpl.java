package gov.ca.emsa.pulse.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.JAXBElement;

import gov.ca.emsa.pulse.service.SOAPToJSONService;
import gov.ca.emsa.pulse.common.domain.DocumentQuery;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;

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
	
	private final static String PATIENT_ID_PARAMETER = "$XDSDocumentEntryPatientId";
	private final static String DOCUMENT_STATUS_PARAMETER = "$XDSDocumentEntryStatus";
	private final static String STATUS_TYPE_APPROVED = "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved";
	private final static String FIND_DOCUMENTS_QUERY_ID = "urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d";
	private final static String GET_DOCUMENTS_QUERY_ID = "urn:uuid:5c4f972b-d56b-40ac-a5fcc8ca9b40b9d4";
	
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
	
	public DocumentQuery convertToDocumentQuery(AdhocQueryRequest request){
		DocumentQuery docQuery = new DocumentQuery();
		List<SlotType1> slots = request.getAdhocQuery().getSlot();
		String patientId = null;
		String documentStatusOriginal;
		String[] documentStatusList = null;
		for(SlotType1 slot : slots){
			if(slot.getName().equals(PATIENT_ID_PARAMETER)){
				patientId = slot.getValueList().getValue().get(0);
			}else if(slot.getName().equals(DOCUMENT_STATUS_PARAMETER)){
				documentStatusOriginal = slot.getValueList().getValue().get(0);
				if(documentStatusOriginal.startsWith("(") && documentStatusOriginal.endsWith(")")){
					documentStatusOriginal.replace("(", "").replace(")", "");
					documentStatusList = documentStatusOriginal.split(",");
					for(String documentString : documentStatusList){
						documentString.replace("'", "").replace("'", "");
					}
				}
			}
		}
		docQuery.setPatientId(patientId);
		docQuery.setDocumentStatuses(documentStatusList);
		return docQuery;
	}

}
