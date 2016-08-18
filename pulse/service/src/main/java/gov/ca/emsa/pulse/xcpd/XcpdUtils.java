package gov.ca.emsa.pulse.xcpd;

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
import gov.ca.emsa.pulse.xcpd.soap.DiscoveryResponseSoapBody;
import gov.ca.emsa.pulse.xcpd.soap.DiscoveryResponseSoapEnvelope;
import gov.ca.emsa.pulse.xcpd.soap.header.Action;
import gov.ca.emsa.pulse.xcpd.soap.header.CorrelationTimeToLive;
import gov.ca.emsa.pulse.xcpd.soap.header.RelatesTo;
import gov.ca.emsa.pulse.xcpd.soap.header.DiscoveryResponseSoapHeader;

import java.util.ArrayList;

public class XcpdUtils {
	
	public static DiscoveryResponseSoapEnvelope generateQueryResponse(String givenInput, String familyInput){
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
}
