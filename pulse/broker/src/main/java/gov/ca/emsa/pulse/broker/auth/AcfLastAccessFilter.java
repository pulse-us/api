package gov.ca.emsa.pulse.broker.auth;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.dao.AlternateCareFacilityDAO;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.manager.AlternateCareFacilityManager;
import gov.ca.emsa.pulse.service.UserUtil;

public class AcfLastAccessFilter extends GenericFilterBean {
	private static final Logger logger = LogManager.getLogger(AcfLastAccessFilter.class);
	@Autowired private AlternateCareFacilityManager acfManager;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		CommonUser user = UserUtil.getCurrentUser();
		if(user != null) {
			//update last read time for the acf in the header
			if(user != null && user.getAcf() != null 
					&& user.getAcf().getId() != null) {
				AlternateCareFacilityDTO acf = acfManager.getById(user.getAcf().getId());
				if(acf != null) {
					//the ACF could have been deleted between the last time a request
					//was made using it and this request... so have to check for null
					acf.setLastReadDate(new Date());
					acfManager.update(acf);
				}
			}
		} else {
			logger.error("There was no user object in the current security context.");
		}
		
		chain.doFilter(req, res);
	}

	public AlternateCareFacilityManager getAcfManager() {
		return acfManager;
	}

	public void setAcfManager(AlternateCareFacilityManager acfManager) {
		this.acfManager = acfManager;
	}

//	@Override
//	public void init(FilterConfig filterConfig) throws ServletException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void destroy() {
//		// TODO Auto-generated method stub
//		
//	}
}
