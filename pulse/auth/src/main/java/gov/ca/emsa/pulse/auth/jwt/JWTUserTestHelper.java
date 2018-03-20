package gov.ca.emsa.pulse.auth.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

import org.hamcrest.CoreMatchers;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;

import gov.ca.emsa.pulse.auth.permission.GrantedPermission;
import gov.ca.emsa.pulse.auth.user.JWTAuthenticatedUser;

public class JWTUserTestHelper {
    public static MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    protected static final Pattern p = Pattern.compile("<id>([0-9]+)</id>");

    public static final ResultMatcher authorized = status().is(CoreMatchers.not(CoreMatchers.is(401)));

    public static void setCurrentUser(String permission, Long liferayStateId, Long liferayAcfId) {
        setCurrentUser(permission, liferayStateId, liferayAcfId, "testuser");
    }

    public static void setCurrentUser(String permission, Long liferayStateId, Long liferayAcfId, String subjectName) {
        JWTAuthenticatedUser jwt = new JWTAuthenticatedUser();
        jwt.setId(1L);
        jwt.setAuthenticated(true);
        jwt.addPermission(new GrantedPermission(permission));
        jwt.setLiferayStateId(liferayStateId);
        jwt.setLiferayAcfId(liferayAcfId);
        jwt.setSubjectName(subjectName);
        SecurityContextHolder.getContext().setAuthentication(jwt);
    }

    public static JWTAuthenticatedUser getCurrentUser() {
        return (JWTAuthenticatedUser) SecurityContextHolder.getContext().getAuthentication();
    }

    public static void setNullUser() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    public static void setAdmin() {
        setAdmin("adminuser");
    }

    public static void setOrgAdmin() {
        setOrgAdmin("orgadminuser");
    }

    public static void setProvider() {
        setProvider("provideruser");
    }

    public static void setAdmin(String user) {
        JWTUserTestHelper.setCurrentUser("ROLE_ADMIN", 999999L, 99999L, user); // the
    }

    public static void setOrgAdmin(String user) {
        JWTUserTestHelper.setCurrentUser("ROLE_ORG_ADMIN", 9009L, 100L, user); // the
    }

    public static void setProvider(String user) {
        JWTUserTestHelper.setCurrentUser("ROLE_PROVIDER", 9009L, 100L, user); // the
    }

    public static void testPatternProvider(MockMvc mockMvc, ObjectWriter ow, String url, boolean isGet)
            throws Exception {
        testPatternProvider(mockMvc, ow, url, isGet, null);
    }

    public static void testPatternOrgAdmin(MockMvc mockMvc, ObjectWriter ow, String url, boolean isGet)
            throws Exception {
        testPatternOrgAdmin(mockMvc, ow, url, isGet, null);
    }

    public static void testPatternProvider(MockMvc mockMvc, ObjectWriter ow, String url, boolean isGet, Object content)
            throws Exception {
        setNullUser();
        mockMvc.perform(isGet ? get(url)
                : content == null ? post(url)
                        : post(url).contentType(contentType).content(ow.writeValueAsString(content)))
                .andExpect(status().isUnauthorized());
        setAdmin();
        mockMvc.perform(isGet ? get(url)
                : content == null ? post(url)
                        : post(url).contentType(contentType).content(ow.writeValueAsString(content)))
                .andExpect(authorized);
        setOrgAdmin();
        mockMvc.perform(isGet ? get(url)
                : content == null ? post(url)
                        : post(url).contentType(contentType).content(ow.writeValueAsString(content)))
                .andExpect(status().isUnauthorized());
        setProvider();
        mockMvc.perform(isGet ? get(url)
                : content == null ? post(url)
                        : post(url).contentType(contentType).content(ow.writeValueAsString(content)))
                .andExpect(authorized);
    }

    public static void testPatternOrgAdmin(MockMvc mockMvc, ObjectWriter ow, String url, boolean isGet, Object content)
            throws Exception {
        setNullUser();
        mockMvc.perform(isGet ? get(url)
                : content == null ? post(url)
                        : post(url).contentType(contentType).content(ow.writeValueAsString(content)))
                .andExpect(status().isUnauthorized());
        setAdmin();
        mockMvc.perform(
                isGet ? get(url) : content == null ? post(url) : post(url).contentType(ow.writeValueAsString(content)))
                .andExpect(authorized);
        setOrgAdmin();
        mockMvc.perform(
                isGet ? get(url) : content == null ? post(url) : post(url).contentType(ow.writeValueAsString(content)))
                .andExpect(authorized);
        setProvider();
        mockMvc.perform(
                isGet ? get(url) : content == null ? post(url) : post(url).contentType(ow.writeValueAsString(content)))
                .andExpect(status().isUnauthorized());
    }

    public static void testPatternProviderWithUser(MockMvc mockMvc, ObjectWriter ow, String endpoint, boolean b,
            String user) throws JsonProcessingException, Exception {
        testPatternProviderWithUser(mockMvc, ow, endpoint, b, null, user);

    }

    public static void testPatternProviderWithUser(MockMvc mockMvc, ObjectWriter ow, String url, boolean isGet,
            Object content, String user) throws JsonProcessingException, Exception {
        setNullUser();
        mockMvc.perform(isGet ? get(url)
                : content == null ? post(url)
                        : post(url).contentType(contentType).content(ow.writeValueAsString(content)))
                .andExpect(status().isUnauthorized());
        setAdmin(user);
        mockMvc.perform(isGet ? get(url)
                : content == null ? post(url)
                        : post(url).contentType(contentType).content(ow.writeValueAsString(content)))
                .andExpect(authorized);
        setAdmin(user + "somethingdifferent");
        mockMvc.perform(isGet ? get(url)
                : content == null ? post(url)
                        : post(url).contentType(contentType).content(ow.writeValueAsString(content)))
                .andExpect(status().isUnauthorized());
        setOrgAdmin(user);
        mockMvc.perform(isGet ? get(url)
                : content == null ? post(url)
                        : post(url).contentType(contentType).content(ow.writeValueAsString(content)))
                .andExpect(status().isUnauthorized());
        setProvider(user);
        mockMvc.perform(isGet ? get(url)
                : content == null ? post(url)
                        : post(url).contentType(contentType).content(ow.writeValueAsString(content)))
                .andExpect(authorized);
        setProvider(user + "somethingdifferent");
        mockMvc.perform(isGet ? get(url)
                : content == null ? post(url)
                        : post(url).contentType(contentType).content(ow.writeValueAsString(content)))
                .andExpect(status().isUnauthorized());
    }

}
