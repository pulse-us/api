package gov.ca.emsa.pulse.broker.auth;

import gov.ca.emsa.pulse.auth.user.JWTAuthenticatedUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpRequestUserFilter extends GenericFilterBean {
	private static final Logger logger = LogManager.getLogger(HttpRequestUserFilter.class);

	List<String> exemptions;
	ObjectMapper jsonMapper;

	public HttpRequestUserFilter() {
		exemptions = new ArrayList<String>();
		exemptions.add("/health");
		//TODO: add others here

		jsonMapper = new ObjectMapper();
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		if(exemptions.contains(request.getRequestURI().trim())) {
			chain.doFilter(req, res);
		} else {
			String userHeader = request.getHeader("User");
			logger.debug("User Header " + userHeader);
			if (userHeader == null){
				SecurityContextHolder.getContext().setAuthentication(null);
				throw new ServletException("No header found with the name 'User'");
			} else {
				JWTAuthenticatedUser authenticatedUser = jsonMapper.readValue(userHeader, JWTAuthenticatedUser.class);
				if (authenticatedUser != null){
					SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
					chain.doFilter(req, res); //continue
					SecurityContextHolder.getContext().setAuthentication(null);
				} else {
					SecurityContextHolder.getContext().setAuthentication(null);
					throw new IOException("A JSON object could not created from the User header with value '" + userHeader + "'.");
				}
			}
		}
	}
}
