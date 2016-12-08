package gov.ca.emsa.pulse.cten;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.common.domain.Address;
import gov.ca.emsa.pulse.common.domain.Endpoint;
import gov.ca.emsa.pulse.common.domain.EndpointMimeType;
import gov.ca.emsa.pulse.common.domain.EndpointStatus;
import gov.ca.emsa.pulse.common.domain.EndpointType;
import gov.ca.emsa.pulse.common.domain.Location;
import gov.ca.emsa.pulse.common.domain.LocationStatus;
import gov.ca.emsa.pulse.cten.domain.EndpointMetadata;
import gov.ca.emsa.pulse.cten.domain.EndpointResource;
import gov.ca.emsa.pulse.cten.domain.EndpointWrapper;
import gov.ca.emsa.pulse.cten.domain.LocationResource;
import gov.ca.emsa.pulse.cten.domain.LocationWrapper;

public class CtenToPulseConverter {
	public static List<Location> convertLocations(LocationWrapper ctenLocs) {
		if(ctenLocs == null || ctenLocs.getEntry() == null || ctenLocs.getEntry().size() == 0) {
			return new ArrayList<Location>();
		}
		List<Location> result = new ArrayList<Location>(ctenLocs.getEntry().size());
		for(gov.ca.emsa.pulse.cten.domain.Location entry : ctenLocs.getEntry()) {
			result.add(convertLocation(entry));
		}
		return result;
	} 
	
	public static Location convertLocation(gov.ca.emsa.pulse.cten.domain.Location ctenLoc) {
		if(ctenLoc.getResource() == null) {
			return null;
		}
		
		Location result = new Location();
		LocationResource ctenResource = ctenLoc.getResource();
		result.setExternalId(ctenResource.getId());
		if(ctenResource.getAddress() != null) {
			Address locAddr = new Address();
			locAddr.getLines().add(ctenResource.getAddress().getLine());
			locAddr.setCity(ctenResource.getAddress().getCity());
			locAddr.setState(ctenResource.getAddress().getState());
			locAddr.setZipcode(ctenResource.getAddress().getPostalCode());
			locAddr.setCountry("USA");
			result.setAddress(locAddr);
		}
		result.setDescription(ctenResource.getDescription());
		if(ctenResource.getMeta().getLastUpdated() != null) {
			result.setExternalLastUpdateDate(new Date(ctenResource.getMeta().getLastUpdated()));
		}
		result.setName(ctenResource.getName());
		if(ctenResource.getManagingOrganization() != null) {
			result.setParentOrgName(ctenResource.getManagingOrganization().getDisplay());
		}
		LocationStatus resultStatus = new LocationStatus();
		resultStatus.setName(ctenResource.getStatus());
		result.setStatus(resultStatus);
		if(ctenResource.getType() != null) {
			if(ctenResource.getType().getCoding() != null && 
				ctenResource.getType().getCoding().size() > 0) {
				result.setType(ctenResource.getType().getCoding().get(0).getDisplay());
			}
		} else if(ctenResource.getPhysicalType() != null && 
				ctenResource.getPhysicalType().getCoding().size() > 0) {
			result.setType(ctenResource.getPhysicalType().getCoding().get(0).getDisplay());;
		}
		if(ctenResource.getEndpoint() != null && ctenResource.getEndpoint().size() > 0) {
			for(EndpointMetadata ctenEndpointMeta : ctenResource.getEndpoint()) {
				//expecting a string of this format "Endpoint/19"
				int lastSlash = ctenEndpointMeta.getUrl().indexOf("/");
				if(lastSlash >= 0) {
					String endpointCtenId = ctenEndpointMeta.getUrl().substring(lastSlash+1);
					Endpoint endpoint = new Endpoint();
					endpoint.setExternalId(endpointCtenId);
					result.getEndpoints().add(endpoint);
				}
			}
		}
		
		return result;
	}
	
	public static List<Endpoint> convertEndpoints(EndpointWrapper ctenEndpoints) {
		if(ctenEndpoints == null || ctenEndpoints.getEntry() == null || ctenEndpoints.getEntry().size() == 0) {
			return new ArrayList<Endpoint>();
		}
		List<Endpoint> result = new ArrayList<Endpoint>(ctenEndpoints.getEntry().size());
		for(gov.ca.emsa.pulse.cten.domain.Endpoint entry : ctenEndpoints.getEntry()) {
			result.add(convertEndpoint(entry));
		}
		return result;
	}
	
	public static Endpoint convertEndpoint(gov.ca.emsa.pulse.cten.domain.Endpoint ctenEnd) {
		if(ctenEnd.getResource() == null) {
			return null;
		}
		
		Endpoint result = new Endpoint();
		EndpointResource ctenResource = ctenEnd.getResource();
		result.setExternalId(ctenResource.getId());
		result.setAdapter("eHealthExchange");
		EndpointStatus resultStatus = new EndpointStatus();
		resultStatus.setName(ctenResource.getStatus());
		result.setEndpointStatus(resultStatus);
		EndpointType resultType = new EndpointType();
		resultType.setName(ctenResource.getName());
		result.setEndpointType(resultType);
		result.setExternalId(ctenResource.getId());
		if(ctenResource.getMeta() != null && ctenResource.getMeta().getLastUpdated() != null) {
			result.setExternalLastUpdateDate(new Date(ctenResource.getMeta().getLastUpdated()));
		}
		if(ctenResource.getPayloadMimeType() != null && ctenResource.getPayloadMimeType().size() > 0) {
			for(String mimeType : ctenResource.getPayloadMimeType()) {
				EndpointMimeType mtDomain = new EndpointMimeType();
				mtDomain.setMimeType(mimeType);
				result.getMimeTypes().add(mtDomain);
			}
		}
		if(ctenResource.getPayloadType() != null && ctenResource.getPayloadType().size() > 0) {
			if(ctenResource.getPayloadType().get(0).getCoding() != null && 
				ctenResource.getPayloadType().get(0).getCoding().size() > 0)
			result.setPayloadType(ctenResource.getPayloadType().get(0).getCoding().get(0).getDisplay());
		}
		result.setPublicKey(ctenResource.getPublicKey());
		result.setUrl(ctenResource.getAddress());
		return result;
	}
}
