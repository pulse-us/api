package gov.ca.emsa.pulse.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.opensaml.xml.io.MarshallingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.ca.emsa.pulse.common.domain.Address;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.Query;
import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.domain.QueryType;
import gov.ca.emsa.pulse.broker.dto.DtoToDomainConverter;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;
import gov.ca.emsa.pulse.common.domain.QueryStatus;
import gov.ca.emsa.pulse.broker.manager.OrganizationManager;
import gov.ca.emsa.pulse.broker.manager.AuditManager;
import gov.ca.emsa.pulse.broker.manager.QueryManager;
import gov.ca.emsa.pulse.broker.manager.impl.JSONUtils;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.broker.saml.SamlGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "search")
@RestController
@RequestMapping("/search")
public class SearchService {

	private static final Logger logger = LogManager.getLogger(SearchService.class);
	@Autowired private SamlGenerator samlGenerator;
	@Autowired private QueryManager searchManager;
	@Autowired private OrganizationManager orgManager;
	public static String dobFormat = "yyyy-MM-dd";
	private DateFormat formatter;
	@Autowired private AuditManager auditManager;

	public SearchService() {
		formatter = new SimpleDateFormat(dobFormat);
	}

	@ApiOperation(value="Query all organizations for patients. This runs asynchronously and returns a query object"
			+ "which can later be used to get the results.")
	@RequestMapping(method = RequestMethod.POST,
		produces="application/json; charset=utf-8",consumes="application/json")
    public @ResponseBody Query searchPatients(@RequestBody(required=true) PatientSearch toSearch) throws JsonProcessingException {

		CommonUser user = UserUtil.getCurrentUser();
		auditManager.addAuditEntry(QueryType.SEARCH_PATIENT, "/search", user.getSubjectName());

		SAMLInput input = new SAMLInput();
		input.setStrIssuer("https://idp.dhv.gov");
		input.setStrNameID("UserBrianLindsey");
		input.setStrNameQualifier("My Website");
		input.setSessionId("abcdedf1234567");

		HashMap<String, String> customAttributes = new HashMap<String,String>();
		customAttributes.put("RequesterFirstName", user.getFirstName());
		customAttributes.put("RequestReason", "Patient is bleeding.");
		customAttributes.put("PatientGivenName", toSearch.getGivenName());
		customAttributes.put("PatientFamilyName", toSearch.getFamilyName());
		customAttributes.put("PatientDOB", toSearch.getDob());
		customAttributes.put("PatientGender", toSearch.getGender());
		customAttributes.put("PatientHomeZip", toSearch.getZip());
		customAttributes.put("PatientSSN", toSearch.getSsn());

		input.setAttributes(customAttributes);

		String samlMessage = null;

		try {
			samlMessage = samlGenerator.createSAML(input);
		} catch (MarshallingException e) {
			logger.error("Could not create SAML from input " + input, e);
		}

		String queryTermsJson = JSONUtils.toJSON(toSearch);

		List<OrganizationDTO> orgsToQuery = orgManager.getAll();
		if(orgsToQuery != null && orgsToQuery.size() > 0) {
			QueryDTO query = new QueryDTO();
			query.setUserId(user.getSubjectName());
			query.setTerms(queryTermsJson);
			query.setStatus(QueryStatus.ACTIVE.name());
			query = searchManager.createQuery(query);
	
			//get the list of organizations		
			for(OrganizationDTO org : orgsToQuery) {
				QueryOrganizationDTO queryOrg = new QueryOrganizationDTO();
				queryOrg.setOrgId(org.getId());
				queryOrg.setQueryId(query.getId());
				queryOrg.setStatus(QueryStatus.ACTIVE.name());
				queryOrg = searchManager.createOrUpdateQueryOrganization(queryOrg);
				query.getOrgStatuses().add(queryOrg);
			}
	
	        QueryDTO initiatedQuery = searchManager.queryForPatientRecords(samlMessage, toSearch, query, user);
	        return DtoToDomainConverter.convert(initiatedQuery);
		}
		return null;
    }
}
