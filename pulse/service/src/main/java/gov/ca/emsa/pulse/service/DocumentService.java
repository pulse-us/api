package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.websocket.server.PathParam;

import org.opensaml.xml.io.MarshallingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import gov.ca.emsa.pulse.service.Document;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "search")
@RestController
@RequestMapping("/search")
public class DocumentService {
	@Autowired private Environment env;

	@ApiOperation(value="Search Documents for a Document.")
	@RequestMapping("/documents")
	public List<Document> searchDocuments(
			@RequestParam(value="documentId", required=true) String DocumentId) throws Exception {
		String url = env.getProperty("brokerDocumentUrl").trim();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = ((UserDetails) auth.getPrincipal()).getUsername();
		System.out.println(username);
		
		MultiValueMap<String,String> parameters = new LinkedMultiValueMap<String,String>();

		RestTemplate query = new RestTemplate();
		Document[] queryResults = query.postForObject(url, parameters, Document[].class);
		ArrayList<Document> DocumentList = new ArrayList<Document>(Arrays.asList(queryResults));

		return DocumentList;
	}

	@ApiOperation(value="Retrieve a specific Document from an organization.")
	@RequestMapping("/{DocumentId}")
	public String getDocumentContents(@PathVariable("DocumentId") Long DocumentId) {

		return null;
	}
}
