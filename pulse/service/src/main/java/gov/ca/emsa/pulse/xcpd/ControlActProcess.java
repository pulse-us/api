package gov.ca.emsa.pulse.xcpd;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;

public class ControlActProcess {
	@XmlAttribute public String classCode;
	@XmlAttribute public String moodCode;
	private Code code;
	private ArrayList<Subject> subjects;
	private QueryAck queryAck;
	private QueryByParameter queryByParamter;

	public ControlActProcess() {
	}

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public ArrayList<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(ArrayList<Subject> subjects) {
		this.subjects = subjects;
	}

	public QueryAck getQueryAck() {
		return queryAck;
	}

	public void setQueryAck(QueryAck queryAck) {
		this.queryAck = queryAck;
	}

	public QueryByParameter getQueryByParamter() {
		return queryByParamter;
	}

	public void setQueryByParamter(QueryByParameter queryByParamter) {
		this.queryByParamter = queryByParamter;
	}
}