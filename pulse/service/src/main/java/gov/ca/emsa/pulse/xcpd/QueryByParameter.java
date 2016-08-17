package gov.ca.emsa.pulse.xcpd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class QueryByParameter {
	
	public QueryId queryId;
	
	public StatusCode statusCode;
	
	public ParameterList parameterList;

	public QueryId getQueryId() {
		return queryId;
	}

	public void setQueryId(QueryId queryId) {
		this.queryId = queryId;
	}

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public ParameterList getParameterList() {
		return parameterList;
	}

	public void setParameterList(ParameterList parameterList) {
		this.parameterList = parameterList;
	}
	
	

}
