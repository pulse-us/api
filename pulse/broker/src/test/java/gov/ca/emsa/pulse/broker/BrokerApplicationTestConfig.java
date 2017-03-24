package gov.ca.emsa.pulse.broker;

import gov.ca.emsa.pulse.broker.domain.EndpointStatusEnum;
import gov.ca.emsa.pulse.broker.manager.impl.PatientQueryService;
import gov.ca.emsa.pulse.broker.util.QueryableEndpointStatusUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

@PropertySource("classpath:/application-test.properties")
@EnableTransactionManagement(proxyTargetClass=true)
@SpringBootApplication(scanBasePackages= {"gov.ca.emsa.pulse.broker.adapter",
		"gov.ca.emsa.pulse.broker.dao.**",
		"gov.ca.emsa.pulse.broker.domain.**",
		"gov.ca.emsa.pulse.broker.dto.**",
		"gov.ca.emsa.pulse.broker.entity.**",
		"gov.ca.emsa.pulse.broker.manager.**",
		"gov.ca.emsa.pulse.broker.saml.**",
		"gov.ca.emsa.pulse.service.**",
		"gov.ca.emsa.pulse.common.soap.**"})
public class BrokerApplicationTestConfig implements EnvironmentAware {
	
	private Environment env;
	
	@Override
	public void setEnvironment(final Environment e) {
		this.env = e;
	}
	
	@Bean
	public org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean entityManagerFactory(){
		org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean bean = new org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean();
		bean.setPersistenceUnitName(env.getProperty("persistenceUnitName"));
		bean.setPersistenceXmlLocation("classpath*:META-INF/persistence-test.xml");
		return bean;
	}
	
	@Bean
	public org.springframework.orm.jpa.JpaTransactionManager transactionManager(){
		org.springframework.orm.jpa.JpaTransactionManager bean = new org.springframework.orm.jpa.JpaTransactionManager();
		bean.setEntityManagerFactory(entityManagerFactory().getObject());
		return bean;
	}

	@Bean
	public org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor(){
		return new org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor();
	}
	
	@Bean
	public QueryableEndpointStatusUtil queryableEndpointStatusUtil() {
		String endpointStatusesToQuery = env.getProperty("endpointStatusesToQuery");
		QueryableEndpointStatusUtil bean = new QueryableEndpointStatusUtil();
		if (StringUtils.isEmpty(endpointStatusesToQuery)) {
			bean.getStatuses().add(EndpointStatusEnum.ACTIVE);
		} else {
			String[] endpointStatusArr = endpointStatusesToQuery.split(",");
			for (int i = 0; i < endpointStatusArr.length; i++) {
				try {
					EndpointStatusEnum endpointStatus = EndpointStatusEnum.valueOf(endpointStatusArr[i].toUpperCase());
					if (endpointStatus != null) {
						bean.getStatuses().add(endpointStatus);
					}
				} catch (IllegalArgumentException ex) {
					System.err.println("Could not find endpoint status from properties file with name " + endpointStatusArr[i]);
				}
			}
		}
		return bean;
	}
	 
	@Bean
    @Scope(scopeName=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public PatientQueryService patientQueryService() {
        return new PatientQueryService();
    }
}