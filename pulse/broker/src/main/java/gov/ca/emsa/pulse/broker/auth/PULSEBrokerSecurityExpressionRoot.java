package gov.ca.emsa.pulse.broker.auth;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.auth.user.JWTAuthenticatedUser;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.manager.AlternateCareFacilityManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.manager.QueryManager;

public class PULSEBrokerSecurityExpressionRoot extends SecurityExpressionRoot
        implements MethodSecurityExpressionOperations {
    private Object filterObject;
    private Object returnObject;

    private AlternateCareFacilityManager acfManager;
    private PatientManager patientManager;
    private QueryManager queryManager;

    public PULSEBrokerSecurityExpressionRoot(Authentication authentication, AlternateCareFacilityManager acfManager,
            PatientManager patientManager, QueryManager queryManager) {
        super(authentication);
        this.acfManager = acfManager;
        this.patientManager = patientManager;
        this.queryManager = queryManager;
    }

    public boolean hasPermissionForPatient(Long patientId) {
        JWTAuthenticatedUser jwtUser = (JWTAuthenticatedUser) SecurityContextHolder.getContext().getAuthentication();
        boolean hasPermission = false;

        if (patientId != null) {
            PatientDTO patient = patientManager.getPatientById(patientId);
            if (patient != null) {
                AlternateCareFacilityDTO acf = patient.getAcf();
                if (acf != null) {
                    if (CommonUser.userHasAuthority(jwtUser, CommonUser.ROLE_ADMIN)) {
                        hasPermission = true;
                    } else if (CommonUser.userHasAuthority(jwtUser, CommonUser.ROLE_ORG_ADMIN)) {
                        hasPermission = false; // getLiferayStateOrg(jwtUser).equals(getLiferayStateOrg(acf));
                    } else if (CommonUser.userHasAuthority(jwtUser, CommonUser.ROLE_PROVIDER)) {
                        hasPermission = jwtUser.getLiferayAcfId().equals(acf.getLiferayAcfId());
                    }
                }
            }
        }
        return hasPermission;
    }

    public boolean hasPermissionForAcf(Long acfId) {
        JWTAuthenticatedUser jwtUser = (JWTAuthenticatedUser) SecurityContextHolder.getContext().getAuthentication();
        boolean hasPermission = false;

        if (acfId != null) {
            AlternateCareFacilityDTO acf = acfManager.getById(acfId);
            if (acf != null) {
                if (CommonUser.userHasAuthority(jwtUser, CommonUser.ROLE_ADMIN)) {
                    hasPermission = true;
                } else if (CommonUser.userHasAuthority(jwtUser, CommonUser.ROLE_ORG_ADMIN)) {
                    hasPermission = jwtUser.getLiferayStateId().equals(acf.getLiferayStateId());
                } else if (CommonUser.userHasAuthority(jwtUser, CommonUser.ROLE_PROVIDER)) {
                    hasPermission = jwtUser.getLiferayAcfId().equals(acf.getLiferayAcfId());
                }
            }
        }

        return hasPermission;
    }

    public boolean hasPermissionForQuery(Long queryId) {
        JWTAuthenticatedUser jwtUser = (JWTAuthenticatedUser) SecurityContextHolder.getContext().getAuthentication();
        boolean hasPermission = false;

        if (queryId != null) {
            QueryDTO query = queryManager.getById(queryId);
            if (query != null) {
                if (CommonUser.userHasAuthority(jwtUser, CommonUser.ROLE_ADMIN)
                        || CommonUser.userHasAuthority(jwtUser, CommonUser.ROLE_PROVIDER)) {
                    hasPermission = query.getUserId() != null && query.getUserId().equals(jwtUser.getSubjectName());
                }
            }
        }
        return hasPermission;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }

}
