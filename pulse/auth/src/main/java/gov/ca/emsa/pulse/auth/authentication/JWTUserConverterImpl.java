package gov.ca.emsa.pulse.auth.authentication;

import gov.ca.emsa.pulse.auth.jwt.JWTConsumer;
import gov.ca.emsa.pulse.auth.jwt.JWTValidationException;
//import gov.ca.emsa.pulse.auth.permission.GrantedPermission;
import gov.ca.emsa.pulse.auth.user.JWTAuthenticatedUser;
import gov.ca.emsa.pulse.auth.user.User;
import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.opensaml.xml.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

@Service
public class JWTUserConverterImpl implements JWTUserConverter {

    @Autowired
    private JWTConsumer jwtConsumer;

    private static final Logger LOG = LoggerFactory.getLogger(JWTUserConverterImpl.class);

    public JWTUserConverterImpl(){}

    public User getAuthenticatedUser(String jwt) throws JWTValidationException {

        JWTAuthenticatedUser user = new JWTAuthenticatedUser();
        user.setAuthenticated(true);

        Map<String, Object> validatedClaims = jwtConsumer.consume(jwt);

        if (validatedClaims == null){
            throw new JWTValidationException("Invalid authentication token.");
        } else {
            /*
             * Handle the standard claim types. These won't be lists of Strings,
             * which we'll be expecting from the claims we are creating ourselves
             */
            Object issuer = validatedClaims.remove("iss");
            Object audience = validatedClaims.remove("aud");
            Object issuedAt = validatedClaims.remove("iat");
            Object notBefore = validatedClaims.remove("nbf");
            Object expires = validatedClaims.remove("exp");
            Object jti = validatedClaims.remove("jti");
            //Object typ = validatedClaims.remove("typ");

            String subject = (String) validatedClaims.remove("sub");

            LOG.info( "jwt claims" );
            LOG.info( issuer.toString() );
            LOG.info( audience.toString() );
            //LOG.info( issuedAt.toString() );
            //LOG.info( notBefore.toString() );
            LOG.info( expires.toString() );
            //LOG.info( jti.toString() );
            //            LOG.info( typ.toString());

            user.setSubjectName(subject);

            List<String> authorities = (List<String>) validatedClaims.get("Authorities");
            List<String> identityInfo = (List<String>) validatedClaims.get("Identity");

            for (String claim: authorities){
                //GrantedPermission permission = new GrantedPermission(claim);
                user.addPermission(claim);
            }

            String user_id = identityInfo.get(0);
            String username = identityInfo.get(1);
            String full_name = identityInfo.get(2);
            String pulseUserId = null;
            if(identityInfo.size() > 3) {
                pulseUserId = identityInfo.get(3);
            }

            user.setuser_id(user_id);
            user.setusername(username);
            user.setfull_name(full_name);
            if(identityInfo.size() > 3) {
                user.setPulseUserId(pulseUserId);
            }

            if (identityInfo.size() > 4) {
                String acfObjStr = identityInfo.get(4);
                try {
                    ObjectReader reader = new ObjectMapper().reader().forType(AlternateCareFacility.class);
                    AlternateCareFacility acf = reader.readValue(acfObjStr);
                    user.setAcf(acf);
                } catch(JsonProcessingException ex) {
                    LOG.error("Could not read '" + acfObjStr + "' as AlternateCareFacility", ex);
                } catch(IOException io) {
                    LOG.error("Could not read '" + acfObjStr + "' as AlternateCareFacility", io);
                }
            }

            LOG.info(user.toString());
        }
        return user;
    }
}
