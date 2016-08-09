package gov.ca.emsa.pulse.broker.app;

import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import gov.ca.emsa.pulse.broker.cache.DirectoryRefreshManager;
import gov.ca.emsa.pulse.broker.dao.AlternateCareFacilityDAO;
import gov.ca.emsa.pulse.broker.auth.AcfLastAccessFilter;
import gov.ca.emsa.pulse.broker.cache.CacheCleanupJob;
import gov.ca.emsa.pulse.broker.manager.AlternateCareFacilityManager;
import gov.ca.emsa.pulse.broker.manager.OrganizationManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.manager.QueryManager;
import gov.ca.emsa.pulse.broker.manager.impl.DocumentQueryService;
import gov.ca.emsa.pulse.broker.manager.impl.PatientQueryService;

@PropertySource("classpath:/application.properties")
@EnableTransactionManagement(proxyTargetClass=true)
@SpringBootApplication(scanBasePackages= {"gov.ca.emsa.pulse.broker.**",
		"gov.ca.emsa.pulse.service.**"})
public class BrokerApplication implements EnvironmentAware {
	
	public static void main(String[] args) {
		SpringApplication.run(BrokerApplication.class, args);
	}
	
	@Autowired private OrganizationManager organizationManager;
	@Autowired private AlternateCareFacilityManager acfManager;
	@Autowired private PatientManager patientManager;
	@Autowired private QueryManager queryManager;
	@Autowired private AlternateCareFacilityDAO acfDao;
	@Autowired private Environment env;
	
	@Override
	public void setEnvironment(final Environment e) {
		this.env = e;
	}
	
	//TODO: the env is still null when the entity manager bean gets called and 
	//i really have no idea why. This is a short-term fix. The longer answer is that
	//spring boot really does not need the persistence.xml and the db stuff should be
	//configured in application.properties
	@Bean
	public org.springframework.orm.jpa.LocalEntityManagerFactoryBean entityManagerFactory(){
		org.springframework.orm.jpa.LocalEntityManagerFactoryBean bean = new org.springframework.orm.jpa.LocalEntityManagerFactoryBean();
		//bean.setPersistenceUnitName(env.getProperty("persistenceUnitName"));
		bean.setPersistenceUnitName("pulse");
		return bean;
	}

	@Bean
	public org.springframework.orm.jpa.JpaTransactionManager transactionManager(){
		org.springframework.orm.jpa.JpaTransactionManager bean = new org.springframework.orm.jpa.JpaTransactionManager();
		bean.setEntityManagerFactory(entityManagerFactory().getObject());
		return bean;
	}

	@Bean
	public CacheCleanupJob queryCacheManager() {
		int queryCacheExpirationMinutes = new Integer(env.getProperty("queryCacheExpireMinutes").trim());
		long queryCacheExpirationMillis = queryCacheExpirationMinutes * 60 * 1000;
		int queryCacheCleanupMinutes = new Integer(env.getProperty("queryCacheCleanupMinutes").trim());
		long queryCacheCleanupMillis = queryCacheCleanupMinutes * 60 * 1000;
		
		CacheCleanupJob task = null;

		if(queryCacheExpirationMinutes > 0) {
			task = new CacheCleanupJob();
			task.setExpirationMillis(queryCacheExpirationMillis);
			task.setManager(queryManager);
			
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(task, 0, queryCacheCleanupMillis);

		}
		
		return task;
	}
	
	@Bean
	public CacheCleanupJob acfCacheManager() {
		int acfCacheExpirationMinutes = new Integer(env.getProperty("acfCacheExpireMinutes").trim());
		long acfCacheExpirationMillis = acfCacheExpirationMinutes * 60 * 1000;
		int acfCacheCleanupMinutes = new Integer(env.getProperty("acfCacheCleanupMinutes").trim());
		long acfCacheCleanupMillis = acfCacheCleanupMinutes * 60 * 1000;
		
		CacheCleanupJob task = null;

		if(acfCacheExpirationMinutes > 0) {
			task = new CacheCleanupJob();
			task.setExpirationMillis(acfCacheExpirationMillis);
			task.setManager(acfManager);
			
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(task, 0, acfCacheCleanupMillis);

		}
		
		return task;
	}
	
	@Bean
	public CacheCleanupJob patientCacheManager() {
		int patientCacheExpirationMinutes = new Integer(env.getProperty("patientCacheExpireMinutes").trim());
		long patientCacheExpirationMillis = patientCacheExpirationMinutes * 60 * 1000;
		int patientCacheCleanupMinutes = new Integer(env.getProperty("patientCacheCleanupMinutes").trim());
		long patientCacheCleanupMillis = patientCacheCleanupMinutes * 60 * 1000;
		
		CacheCleanupJob task = null;

		if(patientCacheExpirationMinutes > 0) {
			task = new CacheCleanupJob();
			task.setExpirationMillis(patientCacheExpirationMillis);
			task.setManager(patientManager);
			
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(task, 0, patientCacheCleanupMillis);

		}
		
		return task;
	}
	
	@Bean
	public DirectoryRefreshManager directoryRefreshManager() {
		int directoryRefresh = new Integer(env.getProperty("directoryRefreshSeconds").trim());
		long directoryRefreshExpirationMillis = directoryRefresh * 1000;
		
		DirectoryRefreshManager qcTask = null;

		if(directoryRefresh > 0) {
			qcTask = new DirectoryRefreshManager();
			qcTask.setManager(organizationManager);
			qcTask.setExpirationMillis(directoryRefreshExpirationMillis);
			qcTask.setDirectoryServicesUrl(env.getProperty("directoryServicesUrl"));
			
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(qcTask, 0, directoryRefreshExpirationMillis);

		}
		
		return qcTask;
	}
	
	   @Bean
	    public FilterRegistrationBean acfLastAccessFilter() {
		   AcfLastAccessFilter filter = new AcfLastAccessFilter();
		   filter.setAcfManager(acfManager);
	        FilterRegistrationBean registration = new FilterRegistrationBean();
	        registration.setFilter(filter);
	        registration.setOrder(2);
	        return registration;
	    }
	   
	@Bean
    @Scope(scopeName=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public PatientQueryService patientQueryService() {
        return new PatientQueryService();
    }
	
	@Bean
    @Scope(scopeName=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public DocumentQueryService documentQueryService() {
		return new DocumentQueryService();
	}
}
