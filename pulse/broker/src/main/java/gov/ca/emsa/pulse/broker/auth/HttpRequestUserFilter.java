package gov.ca.emsa.pulse.broker.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.ca.emsa.pulse.broker.domain.JWTAuthenticatedUser;

@Component
public class HttpRequestUserFilter implements Filter {
	ObjectMapper jsonMapper;
	
	public HttpRequestUserFilter() {
		jsonMapper = new ObjectMapper();
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
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
