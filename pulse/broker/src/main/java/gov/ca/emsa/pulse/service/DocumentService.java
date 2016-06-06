package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.pulse.broker.domain.Document;
import gov.ca.emsa.pulse.broker.domain.Patient;
import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.DtoToDomainConverter;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.manager.DocumentManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "documents")
@RestController
@RequestMapping("/documents")
public class DocumentService {
	@Autowired DocumentManager docManager;
	@Autowired PatientManager patientManager;
	
		@ApiOperation(value="Query a specific organization about a specific person.")
		@RequestMapping("")
	    public List<Document> searchDocuments(
	    		@RequestParam(value="patientId", required=true) String patientId) throws Exception {
			PatientDTO toQuery = patientManager.getPatientById(new Long(patientId));
			List<Document> results = new ArrayList<Document>();
			if(toQuery != null) {
				List<DocumentDTO> docResults = docManager.queryDocumentsForPatient(toQuery);
				for(DocumentDTO docResult : docResults) {
					Document doc = DtoToDomainConverter.convert(docResult);
					results.add(doc);
				}
			}
			return results;
	    }
		
		@ApiOperation(value="Retrieve a specific document from an organization.")
		@RequestMapping("/{documentId}")
		public String getDocumentContents(@PathVariable("documentId") Long documentId) {
			return docManager.getDocumentById(documentId);
		}
}
