package gov.ca.emsa.pulse.common.soap;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import gov.ca.emsa.pulse.common.domain.DocumentQuery;
import gov.ca.emsa.pulse.common.domain.DocumentRetrieve;
import gov.ca.emsa.pulse.common.domain.PatientRecord;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;

import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.springframework.stereotype.Service;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201304UV02Patient;
import org.hl7.v3.PRPAMT201304UV02Person;

@Service
public class SOAPToJSONServiceImpl implements SOAPToJSONService {
	
	private final static String PATIENT_ID_PARAMETER = "$XDSDocumentEntryPatientId";
	private final static String DOCUMENT_STATUS_PARAMETER = "$XDSDocumentEntryStatus";
	
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
	
	public List<PatientRecord> convertToPatientRecords(PRPAIN201310UV02 request){
		List<PatientRecord> result = new ArrayList<PatientRecord>();
		
		List<PRPAIN201310UV02MFMIMT700711UV01Subject1> subjects = request.getControlActProcess().getSubject();
		if(subjects != null && subjects.size() > 0) {
			for(PRPAIN201310UV02MFMIMT700711UV01Subject1 subject : subjects) {
				PatientRecord patientRecord = new PatientRecord();
				PRPAIN201310UV02MFMIMT700711UV01Subject2 currSubject = subject.getRegistrationEvent().getSubject1();
				JAXBElement<PRPAMT201304UV02Person> patientPerson = currSubject.getPatient().getPatientPerson();
				List<PNExplicit> names = patientPerson.getValue().getName();
				for(PNExplicit name : names) {
					List<Serializable> nameParts = name.getContent();
					for(Serializable namePart : nameParts) {
						if(namePart instanceof JAXBElement<?>) {
							if(((JAXBElement<?>) namePart).getName().getLocalPart().equalsIgnoreCase("given")) {
								patientRecord.setGivenName(((JAXBElement<EnExplicitGiven>)namePart).getValue().getContent());
							}
						} else if(namePart instanceof JAXBElement<?>) {
							if(((JAXBElement<?>) namePart).getName().getLocalPart().equalsIgnoreCase("family")) {
								patientRecord.setFamilyName(((JAXBElement<EnExplicitFamily>)namePart).getValue().getContent());
							}
						}
					}
				}
				
				//TODO add address, birth date, etc
			}
		}
		
		return result;
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
	
	public DocumentRetrieve convertToDocumentSetRequest(RetrieveDocumentSetRequestType request){
		DocumentRetrieve dr = new DocumentRetrieve();
		dr.setDocRequests(request.getDocumentRequest());
		return dr;
	}

}
