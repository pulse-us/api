package gov.ca.emsa.pulse.broker.app;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.ca.emsa.pulse.broker.domain.User;

public class HttpRequestUserFilter extends GenericFilterBean {
	ObjectMapper jsonMapper;
	
	public HttpRequestUserFilter() {
		jsonMapper = new ObjectMapper();
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		String userHeader = request.getHeader("User");
		
		if (userHeader == null){
			SecurityContextHolder.getContext().setAuthentication(null);
			throw new ServletException("No header found with the name 'User'");
		} else {
			User authenticatedUser = jsonMapper.readValue(userHeader, User.class);
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
