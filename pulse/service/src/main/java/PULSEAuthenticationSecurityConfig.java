import java.util.ArrayList;
import java.util.List;

import gov.ca.emsa.pulse.auth.authentication.JWTUserConverter;
import gov.ca.emsa.pulse.auth.filter.JWTAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"gov.ca.emsa.pulse.auth.**", "gov.ca.emsa.pulse.auth.jwt.*"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)})
public class PULSEAuthenticationSecurityConfig extends
		WebSecurityConfigurerAdapter {
	
	@Autowired private JWTUserConverter userConverter;
	
	public PULSEAuthenticationSecurityConfig() {
		super(true);
	}
	
	@Autowired
	protected void configureGlobal(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/**").hasRole("USER").and()
		.addFilter(new JWTAuthenticationFilter(userConverter)).headers();
	}
	
	@Bean
	public MappingJackson2HttpMessageConverter jsonConverter(){
		
		MappingJackson2HttpMessageConverter bean = new MappingJackson2HttpMessageConverter();
		
		bean.setPrefixJson(false);
		
		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		
		bean.setSupportedMediaTypes(mediaTypes);
		
		return bean;
	}
	
}