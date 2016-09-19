package gov.ca.emsa.pulse.service.impl;

import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.PatientRecord;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;

import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201304UV02Patient;
import org.hl7.v3.PRPAMT201304UV02Person;
import org.hl7.v3.PRPAMT201310UV02Subject;
import org.springframework.stereotype.Service;

import gov.ca.emsa.pulse.service.JSONToSOAPService;

@Service
public class JSONToSOAPServiceImpl implements JSONToSOAPService{
	public PRPAIN201310UV02 convertPatientRecordListToSOAPResponse(List<PatientRecord> patientRecords){
		PRPAIN201310UV02 returnSOAP = new PRPAIN201310UV02();
		List<PRPAIN201310UV02MFMIMT700711UV01Subject1> subjects = new ArrayList<PRPAIN201310UV02MFMIMT700711UV01Subject1>();
		PRPAIN201310UV02MFMIMT700711UV01ControlActProcess cap = new PRPAIN201310UV02MFMIMT700711UV01ControlActProcess();
		for(PatientRecord record : patientRecords){
			PRPAIN201310UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201310UV02MFMIMT700711UV01Subject1();
			PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent registrationEvent = new PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent();
			PRPAIN201310UV02MFMIMT700711UV01Subject2 subject1 = new PRPAIN201310UV02MFMIMT700711UV01Subject2();
			PRPAMT201304UV02Patient patient = new PRPAMT201304UV02Patient();
			JAXBElement<EnExplicitGiven> given = new JAXBElement(new QName("given"), String.class, record.getGivenName());
			JAXBElement<EnExplicitFamily> family = new JAXBElement(new QName("family"), String.class, record.getFamilyName());
			PNExplicit pnGiven = new PNExplicit();
			PNExplicit pnFamily = new PNExplicit();
			pnGiven.getContent().add(given);
			pnFamily.getContent().add(family);
			PRPAMT201304UV02Person patientPerson1 = new PRPAMT201304UV02Person();
			patientPerson1.getName().add(pnGiven);
			patientPerson1.getName().add(pnFamily);
			JAXBElement<PRPAMT201304UV02Person> patientPerson = new JAXBElement(new QName("patient"), PRPAMT201304UV02Person.class, patientPerson1);
			patient.setPatientPerson(patientPerson);
			subject1.setPatient(patient);
			registrationEvent.setSubject1(subject1);
			subject.setRegistrationEvent(registrationEvent);
			subjects.add(subject);
		}
		cap.getSubject().addAll(subjects);
		returnSOAP.setControlActProcess(cap);
		return returnSOAP;
	}
	
	public AdhocQueryResponse convertDocumentListToSOAPResponse(List<Document> doc){
		RegistryObjectListType rol = new RegistryObjectListType();
		ExtrinsicObjectType eo = new ExtrinsicObjectType();
		
	}
}
