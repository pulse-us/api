package gov.ca.emsa.pulse.broker.dao;

import static org.junit.Assert.*;

import java.util.List;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.domain.QueryType;
import gov.ca.emsa.pulse.broker.dto.AuditDTO;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryStatus;
import gov.ca.emsa.pulse.broker.manager.AuditManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
public class AuditDaoTest {
	
	@Autowired AuditDAO auditDao;
	@Autowired AuditManager auditManager;
	
	@Test
	@Transactional
	public void testInsertAudit() {
		
		AuditDTO inserted = auditManager.addAuditEntry(QueryType.CREATE_ACF, "/acf/create", "blindsey");
		
		assertNotNull(inserted);
		
		List<AuditDTO> auditDTO = auditManager.getAll();
		assertEquals(1, auditDTO.size());
		assertEquals("blindsey", auditDTO.get(0).getQuerent());
		assertEquals("CREATE_ACF", auditDTO.get(0).getQueryType());
		assertEquals("/acf/create", auditDTO.get(0).getQuery());
		
	}
}
