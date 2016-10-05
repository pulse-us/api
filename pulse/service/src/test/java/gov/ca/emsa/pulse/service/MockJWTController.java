package gov.ca.emsa.pulse.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockJWTController {
	
	// JWT generated 9/13/2016, good for 70 days
	private static final String jwt = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJDQUxFTVNBIiwiYXVkIjoiQ0FMRU1TQSIsImV4cCI6MTQ3OTc3MDcwMSwianRpIjoiVFcxbEVLTFZ4X3NRTGR5NnQtY3RyQSIsImlhdCI6MTQ3Mzc3MDcwMSwibmJmIjoxNDczNzcwNDYxLCJzdWIiOiJmYWtlQHNhbXBsZS5jb20iLCJBdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiSWRlbnRpdHkiOlsiRmFrZSIsIlBlcnNvbiIsImZha2VAc2FtcGxlLmNvbSJdfQ.N6Kxy9m0ZXagBsBMy3egOT4Vk9pqM3C6ujFgUlok-8kaupwSF_qt-0Q7_oV6eYOgiaUuUuDTHoU7VkFwKG51DeJFZJYb_rSvy3gAAwDEahePYxNlQGpoc39LlN3EltM1U9rZ9Vj8HlgtE8DMUcmG080D0cZDpFDEdADbEchl8KkIMs_tnhs6Rour4KGymcB5lCHhdVdYH_AMKqZ6qWk2Rh2Ejd6SzRPydmcdR_7jIUKdbsgvjFr0I_l2G_tZggJssGx2zmJmyaMsWGnQOfibojugQiZ16qkpHKPAnG9sdGV6_yfHg8gU9dKx1MJOXkvcatJ3cuLRZIwHJQ-k8zLocA";
	
	@RequestMapping("/jwt")
	public String returnJWT(){
		return "{\"token\": \""+ jwt +"\"}";
	}

}
