package gov.ca.emsa.pulse.xcpd;

import gov.ca.emsa.pulse.xcpd.aqr.AdhocQueryResponse;
import gov.ca.emsa.pulse.xcpd.aqr.Classification;
import gov.ca.emsa.pulse.xcpd.aqr.ExtrinsicObject;
import gov.ca.emsa.pulse.xcpd.aqr.LocalizedString;
import gov.ca.emsa.pulse.xcpd.aqr.RegistryObjectList;
import gov.ca.emsa.pulse.xcpd.aqr.Slot;
import gov.ca.emsa.pulse.xcpd.aqr.Name;
import gov.ca.emsa.pulse.xcpd.aqr.ValueList;
import gov.ca.emsa.pulse.xcpd.prpa.AcceptAckCode;
import gov.ca.emsa.pulse.xcpd.prpa.Acknowledgement;
import gov.ca.emsa.pulse.xcpd.prpa.ControlActProcess;
import gov.ca.emsa.pulse.xcpd.prpa.CreationTime;
import gov.ca.emsa.pulse.xcpd.prpa.InteractionId;
import gov.ca.emsa.pulse.xcpd.prpa.ProcessingCode;
import gov.ca.emsa.pulse.xcpd.prpa.ProcessingModeCode;
import gov.ca.emsa.pulse.xcpd.prpa.Receiver;
import gov.ca.emsa.pulse.xcpd.prpa.Sender;
import gov.ca.emsa.pulse.xcpd.prpa.TargetMessage;
import gov.ca.emsa.pulse.xcpd.prpa.TypeCode;
import gov.ca.emsa.pulse.xcpd.prpa.cap.Code;
import gov.ca.emsa.pulse.xcpd.prpa.cap.QueryAck;
import gov.ca.emsa.pulse.xcpd.prpa.cap.QueryByParameter;
import gov.ca.emsa.pulse.xcpd.prpa.cap.QueryReponseCode;
import gov.ca.emsa.pulse.xcpd.prpa.cap.Subject;
import gov.ca.emsa.pulse.xcpd.prpa.cap.qbp.ParameterList;
import gov.ca.emsa.pulse.xcpd.prpa.cap.qbp.QueryId;
import gov.ca.emsa.pulse.xcpd.prpa.cap.qbp.StatusCode;
import gov.ca.emsa.pulse.xcpd.prpa.cap.qbp.pl.LivingSubjectNameValue;
import gov.ca.emsa.pulse.xcpd.prpa.cap.qbp.pl.LivingSubjBirthTimeValue;
import gov.ca.emsa.pulse.xcpd.prpa.cap.qbp.pl.LivingSubjectAdminGender;
import gov.ca.emsa.pulse.xcpd.prpa.cap.qbp.pl.LivingSubjectAdminGenderValue;
import gov.ca.emsa.pulse.xcpd.prpa.cap.qbp.pl.LivingSubjectBirthTime;
import gov.ca.emsa.pulse.xcpd.prpa.cap.qbp.pl.LivingSubjectId;
import gov.ca.emsa.pulse.xcpd.prpa.cap.qbp.pl.LivingSubjectName;
import gov.ca.emsa.pulse.xcpd.prpa.cap.subj.Custodian;
import gov.ca.emsa.pulse.xcpd.prpa.cap.subj.Patient;
import gov.ca.emsa.pulse.xcpd.prpa.cap.subj.PatientPerson;
import gov.ca.emsa.pulse.xcpd.prpa.cap.subj.ProviderOrganization;
import gov.ca.emsa.pulse.xcpd.prpa.cap.subj.RegistrationEvent;
import gov.ca.emsa.pulse.xcpd.prpa.cap.subj.SubjectOfOne;
import gov.ca.emsa.pulse.xcpd.prpa.cap.subj.SubjectOne;
import gov.ca.emsa.pulse.xcpd.rds.DocumentResponse;
import gov.ca.emsa.pulse.xcpd.rds.RegistryResponse;
import gov.ca.emsa.pulse.xcpd.rds.RetrieveDocumentSetResponse;
import gov.ca.emsa.pulse.xcpd.soap.DiscoveryResponseSoapBody;
import gov.ca.emsa.pulse.xcpd.soap.DiscoveryResponseSoapEnvelope;
import gov.ca.emsa.pulse.xcpd.soap.QueryResponseSoapBody;
import gov.ca.emsa.pulse.xcpd.soap.QueryResponseSoapEnvelope;
import gov.ca.emsa.pulse.xcpd.soap.RetrieveDocumentSetRequestSoapBody;
import gov.ca.emsa.pulse.xcpd.soap.RetrieveDocumentSetRequestSoapEnvelope;
import gov.ca.emsa.pulse.xcpd.soap.RetrieveDocumentSetResponseSoapBody;
import gov.ca.emsa.pulse.xcpd.soap.RetrieveDocumentSetResponseSoapEnvelope;
import gov.ca.emsa.pulse.xcpd.soap.header.Action;
import gov.ca.emsa.pulse.xcpd.soap.header.CorrelationTimeToLive;
import gov.ca.emsa.pulse.xcpd.soap.header.QueryResponseSoapHeader;
import gov.ca.emsa.pulse.xcpd.soap.header.RelatesTo;
import gov.ca.emsa.pulse.xcpd.soap.header.DiscoveryResponseSoapHeader;
import gov.ca.emsa.pulse.xcpd.soap.header.RetrieveDocumentSetRequestSoapHeader;
import gov.ca.emsa.pulse.xcpd.soap.header.RetrieveDocumentSetResponseSoapHeader;

import java.util.ArrayList;


public class XcpdUtils {
	
	public static DiscoveryResponseSoapEnvelope generateDiscoveryResponse(String givenInput, String familyInput){
		PatientDiscoveryResponse pdr = new PatientDiscoveryResponse();
		CreationTime ct = new CreationTime();
		ct.value = (String.valueOf(System.currentTimeMillis()));
		pdr.creationTime = ct;
		InteractionId interId = new InteractionId();
		interId.root = "12345";
		pdr.interactionId = interId;
		ProcessingCode pc = new ProcessingCode();
		pc.code = "P";
		pdr.processingCode = pc;
		ProcessingModeCode pcm = new ProcessingModeCode();
		pcm.code = "T";
		pdr.processingModeCode = pcm;
		AcceptAckCode aac = new AcceptAckCode();
		aac.code = "NE";
		pdr.acceptAckCode = aac;
		
		Id id1 = new Id();
		id1.root = "12345";
		Device device1 = new Device();
		device1.setId(id1);
		Receiver receiver = new Receiver();
		receiver.typeCode = "RCV";
		receiver.device = device1;
		pdr.receiver = receiver;
		
		Id id2 = new Id();
		id2.root = "12345";
		Device device2 = new Device();
		device2.setId(id2);
		Sender sender = new Sender();
		sender.typeCode = "SND";
		sender.device = device2;
		pdr.sender = sender;
		
		// acknowledgement
		Id ackId = new Id();
		ackId.root = "12345";
		ackId.extension = "12345";
		TargetMessage tm = new TargetMessage();
		tm.setId(ackId);
		TypeCode tc = new TypeCode();
		tc.code = "1234";
		Acknowledgement ack = new Acknowledgement();
		ack.setTypeCode(tc);
		ack.setTargetMessage(tm);
		pdr.acknowledgement = ack;
		
		//queryByParameter
		QueryByParameter qbp = new QueryByParameter();
		
		QueryId qid = new QueryId();
		qid.root = "1234";
		qid.extension = "12345";
		StatusCode sc = new StatusCode();
		sc.code = "12345";
		qbp.setQueryId(qid);
		qbp.setStatusCode(sc);
		
		ParameterList pl = new ParameterList();
		
		Id lsidv = new Id();
		lsidv.root = "12345";
		lsidv.extension = "123";
		LivingSubjectId lsid = new LivingSubjectId();
		lsid.semanticsText = "LivingSubject.id";
		lsid.setLsiv(lsidv);
		ArrayList<LivingSubjectId> array = new ArrayList<LivingSubjectId>();
		array.add(lsid);
		pl.livingSubjectId = array;
		
		String given = givenInput;
		String family = familyInput;
		LivingSubjectNameValue lsnv = new LivingSubjectNameValue();
		lsnv.given = given;
		lsnv.family = family;
		LivingSubjectName lsn = new LivingSubjectName();
		lsn.setValue(lsnv);
		lsn.semanticsText = "LivingSubject.name";
		pl.setLivingSubjectName(lsn);
		
		LivingSubjBirthTimeValue lsbtv = new LivingSubjBirthTimeValue();
		lsbtv.value = "12345";
		LivingSubjectBirthTime lsbt = new LivingSubjectBirthTime();
		lsbt.semanticsText = "LivingSubject.birthTime";
		lsbt.setValue(lsbtv);
		pl.setLivingSubjectBirthTime(lsbt);
		
		LivingSubjectAdminGenderValue lsagv = new LivingSubjectAdminGenderValue();
		lsagv.code = "M";
		LivingSubjectAdminGender lsag = new LivingSubjectAdminGender();
		lsag.value = lsagv;
		lsag.semanticsText = "LivingSubject.administrativeGender";
		pl.setLivingSubjectAdministrativeGender(lsag);
		
		qbp.parameterList = pl;
		
		QueryId qid2 = new QueryId();
		qid2.root = "12345";
		qid2.extension = "123";
		QueryReponseCode qrc = new QueryReponseCode();
		qrc.code = "OK";
		QueryAck qa = new QueryAck();
		qa.setQueryId(qid2);
		qa.setQueryResponseCode(qrc);
		
		ControlActProcess cap = new ControlActProcess();
		cap.setQueryByParameter(qbp);
		cap.setQueryAck(qa);
		Code code = new Code();
		code.code = "PRPA_TE201306UV02";
		code.codeSystem = "12345";
		cap.setCode(code);
		
		Subject subject1 = new Subject();
		subject1.typeCode = "SUBJ";
		ArrayList<Subject> subjs = new ArrayList<Subject>();
		
		RegistrationEvent re = new RegistrationEvent();
		re.classCode = "REG";
		re.moodCode = "EVN";
		Id regId = new Id();
		regId.nullFlavor = "NA";
		StatusCode regSc = new StatusCode();
		regSc.code = "active";
		
		SubjectOne s1 = new SubjectOne();
		s1.typeCode = "SBJ";
		
		Patient patient = new Patient();
		patient.classCode = "PAT";
		Id patId = new Id();
		patId.root = "12345";
		patId.extension = "123";
		StatusCode patStatus = new StatusCode();
		patStatus.code = "12345";
		patient.setId(patId);
		patient.setStatusCode(patStatus);
		
		PatientPerson pp = new PatientPerson();
		ProviderOrganization po = new ProviderOrganization();
		SubjectOfOne soo = new SubjectOfOne();
		patient.setPp(pp);
		patient.setProviderOrganization(po);
		patient.setSubjectOf1(soo);
		
		s1.setPatient(patient);
		
		re.setSubject1(s1);
		
		Custodian cust = new Custodian();
		
		re.setCustodian(cust);
		
		subjs.add(subject1);
		cap.subjects = subjs;
		
		QueryAck queryAck = new QueryAck();
		QueryId qid1 = new QueryId();
		qid1.root = "12345";
		qid1.extension = "123";
		
		QueryReponseCode qrc1 = new QueryReponseCode();
		qrc1.code = "OK";
		
		queryAck.setQueryId(qid1);
		queryAck.setQueryResponseCode(qrc1);
		cap.setQueryAck(queryAck);
		cap.setQueryByParameter(qbp);
		
		pdr.controlActProcess = cap;
		
		DiscoveryResponseSoapEnvelope se = new DiscoveryResponseSoapEnvelope();
		DiscoveryResponseSoapHeader sh = new DiscoveryResponseSoapHeader();
		DiscoveryResponseSoapBody sb = new DiscoveryResponseSoapBody();
		sb.PRPA_IN201306UV02 = pdr;
		
		Action action = new Action();
		CorrelationTimeToLive cttl = new CorrelationTimeToLive();
		RelatesTo rt = new RelatesTo();
		
		sh.action = action;
		sh.cttl = cttl;
		sh.relatesTo = rt;
		
		se.sHeader = sh;
		se.sBody = sb;
		
		return se;
	}
	
	public static QueryResponseSoapEnvelope generateQueryResponse(){
		QueryResponseSoapEnvelope se = new QueryResponseSoapEnvelope();
		QueryResponseSoapHeader sh = new QueryResponseSoapHeader();
		QueryResponseSoapBody sb = new QueryResponseSoapBody();
		AdhocQueryResponse aqr = new AdhocQueryResponse();
		RegistryObjectList rol = new RegistryObjectList();
		ExtrinsicObject eo = new ExtrinsicObject();

		Slot slot1 = new Slot();
		ValueList vl1 = new ValueList();
		vl1.value.add("http://localhost:8080/XDS/Repository/08a15a6f-5b4a-42de-8f95-89474f83abdf.xml");
		slot1.valueList = vl1;
		slot1.name = "URI";

		Slot slot2 = new Slot();
		ValueList vl2 = new ValueList();
		vl2.value.add(String.valueOf(System.currentTimeMillis()));
		slot2.valueList = vl2;
		slot2.name = "creationTime";
		
		Slot slot3 = new Slot();
		ValueList vl3 = new ValueList();
		vl3.value.add("12345");
		slot3.valueList = vl3;
		slot3.name = "sourcePatientId";
		
		Classification classification = new Classification();
		Slot slot4 = new Slot();
		ValueList vl4 = new ValueList();
		vl4.value.add("12345");
		slot4.valueList = vl4;
		slot4.name = "codingScheme";
		Name name = new Name();
		LocalizedString ls = new LocalizedString();
		ls.charset = "UTF-8";
		ls.value = "Celebrity";
		name.localizedString = ls;
		classification.name = name;
		classification.slot = slot4;
		
		eo.slots.add(slot1);
		eo.slots.add(slot2);
		eo.slots.add(slot3);
		eo.classification.add(classification);
		
		rol.extrinsicObject = eo;
		aqr.registryObjectList = rol;
		sb.adhocQueryResponse = aqr;
		
		sh.action.mustUnderstand = "1";
		sh.action.action = "urn:ihe:iti:2008:RegistryStoredQueryAsyncResponse";
		sh.messageId.messageId = "urn:uuid:D6C21225-8E7B-454E-9750-821622C099DB";
		sh.relatesTo.relatesTo = "urn:uuid:a02ca8cd-86fa-4afc-a27c-616c183b2055";
		sh.to.mustUnderstand = "1";
		sh.to.to = "http://localhost:2647/XdsService/DocumentConsumerReceiver.svc";
		
		se.body = sb;
		se.header = sh;
		
		return se;
	}
	
	public static RetrieveDocumentSetResponseSoapEnvelope generateDocumentResponse(){
		
		RetrieveDocumentSetResponseSoapEnvelope rdse = new RetrieveDocumentSetResponseSoapEnvelope();
		RetrieveDocumentSetResponseSoapBody rdsb = new RetrieveDocumentSetResponseSoapBody();
		RetrieveDocumentSetResponseSoapHeader rdsh = new RetrieveDocumentSetResponseSoapHeader();
		
		rdsh.action.mustUnderstand = "1";
		rdsh.action.action = "urn:ihe:iti:2008:RegistryStoredQueryAsyncResponse";
		rdsh.messageId.messageId = "urn:uuid:D6C21225-8E7B-454E-9750-821622C099DB";
		rdsh.relatesTo.relatesTo = "urn:uuid:a02ca8cd-86fa-4afc-a27c-616c183b2055";
		rdsh.to.mustUnderstand = "1";
		rdsh.to.to = "http://localhost:2647/XdsService/DocumentConsumerReceiver.svc";
		
		RetrieveDocumentSetResponse rdsr = new RetrieveDocumentSetResponse();
		RegistryResponse rr = new RegistryResponse();
		rr.status = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
		
		DocumentResponse dr = new DocumentResponse();
		dr.repositoryUniqueId = "12345";
		dr.documentUniqueId = "12345";
		dr.mimeType = "text/xml";
		dr.document = "123456789023w09rew98rp9ew8ry";
		
		rdsr.registryReponse = rr;
		rdsr.documentResponse.add(dr);
		
		rdsb.retrieveDocumentSetResponse = rdsr;
		
		rdse.body = rdsb;
		rdse.header = rdsh;
		
		return rdse;
	}
	
}
