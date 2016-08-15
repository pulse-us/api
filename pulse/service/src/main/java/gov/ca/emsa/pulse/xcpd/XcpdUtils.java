package gov.ca.emsa.pulse.xcpd;

import java.util.ArrayList;

public class XcpdUtils {
	
	public static PatientDiscoveryResponse generateQueryResponse(){
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
		pl.setLivingSubjId(array);
		
		Given given = new Given();
		given.setGiven("Brian");
		Family family = new Family();
		family.setFamily("Lindsey");
		LiveSubjNameValue lsnv = new LiveSubjNameValue();
		lsnv.setGiven(given);
		lsnv.setFamily(family);
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
		lsag.setLivingSubjectAdministrativeGender(lsagv);
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
		cap.setQueryByParamter(qbp);
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
		patient.setSc(patStatus);
		
		PatientPerson pp = new PatientPerson();
		ProviderOrganization po = new ProviderOrganization();
		SubjectOfOne soo = new SubjectOfOne();
		patient.setPp(pp);
		patient.setPo(po);
		patient.setSoo(soo);
		
		s1.setPatient(patient);
		
		re.setSo(s1);
		
		Custodian cust = new Custodian();
		
		re.setCustodian(cust);
		
		subjs.add(subject1);
		cap.setSubjects(subjs);
		
		QueryAck queryAck = new QueryAck();
		QueryId qid1 = new QueryId();
		qid1.root = "12345";
		qid1.extension = "123";
		
		QueryReponseCode qrc1 = new QueryReponseCode();
		qrc1.code = "OK";
		
		queryAck.setQueryId(qid1);
		queryAck.setQueryResponseCode(qrc1);
		cap.setQueryAck(queryAck);
		cap.setQueryByParamter(qbp);
		
		pdr.controlActProcess = cap;
		
		return pdr;
	}
}
