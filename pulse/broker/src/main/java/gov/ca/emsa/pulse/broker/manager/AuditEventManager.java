package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.audit.AuditEvent;
import gov.ca.emsa.pulse.broker.domain.QueryType;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.List;

public interface AuditEventManager {
	public List<AuditEventDTO> findAllAuditEvents();
	AuditEventDTO addAuditEventEntry(AuditEventDTO ae);
	public void createAuditEventIG(String outcome, CommonUser user, String endpointUrl, String marshallQueryByParameter, String homeCommunityId) throws UnknownHostException, UnsupportedEncodingException;
	public void createAuditEventDCXGatewayQuery(String outcome, CommonUser user, String endpointUrl, String repositoryUniqueId, String documentUniqueId, String patientId) throws UnknownHostException, UnsupportedEncodingException;
	public void createAuditEventDCXGatewayRetrieve(String outcome, CommonUser user, String endpointUrl, String repositoryUniqueId, String documentUniqueId, String homeCommunityId, String patientId) throws UnknownHostException, UnsupportedEncodingException;
}
