package gov.ca.emsa.pulse.auth.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import gov.ca.emsa.pulse.auth.jwt.JWTAuthorRsaJoseJImpl;
import gov.ca.emsa.pulse.auth.jwt.JWTCreationException;
import gov.ca.emsa.pulse.auth.jwt.JWTValidationException;
import gov.ca.emsa.pulse.auth.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        gov.ca.emsa.pulse.auth.TestConfig.class
})
public class JWTUserConverterImplTest {

    @Autowired
    private JWTUserConverterImpl converter;

    @Autowired
    private JWTAuthorRsaJoseJImpl jwtAuthor;

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(JWTUserConverterImplTest.class);

    @Test
    public void converterConvertsJWTToUser() throws JWTCreationException, JWTValidationException {

        String userId = "user";
        Map<String, Object> jwtAuthorities = new HashMap<String, Object>();
        List<String> auths = new ArrayList<String>();
        auths.add("ROLE_USER");
        jwtAuthorities.put(JWTUserConverter.AUTHORITIES, auths);
        List<String> ids = new ArrayList<String>();
        ids.add("user_id");
        ids.add("username");
        ids.add("auth_source");
        ids.add("full_name");
        ids.add("organization");
        ids.add("purpose_for_use");
        ids.add("role");
        jwtAuthorities.put(JWTUserConverter.IDENTITY, ids);

        String jwt = jwtAuthor.createJWT(userId, jwtAuthorities);
        User user = converter.getAuthenticatedUser(jwt);

        assertNotNull(user);

        assertEquals(user.getuser_id(), "user_id");
        assertEquals(user.getfull_name(), "full_name");
        // assertEquals(user.getSubjectName(), testUser.getSubjectName());
    }

    @Test
    public void converterConvertsRejectsInvalidStringForJWTToUser() {

        String garbage = "Garbage In";

        Boolean throwsException = false;

        try {
            User user = converter.getAuthenticatedUser(garbage);
        } catch (JWTValidationException e) {
            LOG.info("JWT Validation Exception", e);
            LOG.debug("JWT Validation Exception", e);
            throwsException = true;
        }
        assertTrue(throwsException);
    }
}
