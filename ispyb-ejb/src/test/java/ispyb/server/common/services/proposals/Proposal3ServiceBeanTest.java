package ispyb.server.common.services.proposals;

import ispyb.server.common.vos.proposals.Proposal3VO;
import jakarta.ejb.embeddable.EJBContainer;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.junit.*;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Ignore("requires db with data")
public class Proposal3ServiceBeanTest {

    @Inject private Proposal3Service proposalService;

    private static EJBContainer ejbContainer;

    @BeforeClass public static void beforeClass() {
        Properties properties = new Properties();
//        properties.put("jdbc/ispyb", "new://Resource?type=DataSource");
        properties.put("ispyb", "new://Resource?type=DataSource");
        properties.put("ispyb.JdbcDriver", "org.mariadb.jdbc.Driver");
        properties.put("ispyb.JdbcUrl", "jdbc:mariadb://localhost:3306/pydb");
        properties.put("ispyb.UserName", "pxadmin");
        properties.put("ispyb.Password", "pxadmin");
        ejbContainer = EJBContainer.createEJBContainer(properties);
    }

    @AfterClass public static void afterClass() {
        if(ejbContainer != null)
            ejbContainer.close();
    }

    @Before public void before() throws Exception {
        ejbContainer.getContext().bind("inject", this);

    }

    @After public void after() throws Exception {
        ejbContainer.getContext().unbind("inject");
    }

    @Test public void testFindByLoginName() throws Exception {
        List<Proposal3VO> actualProposals = proposalService.findByLoginName("ispybdev");
        assertEquals(1, actualProposals.size());
    }

    @Test public void testFindByPk() throws Exception {
        var result = proposalService.findByPk(8425);
        assertNotNull(result);
    }

    @Test public void testFindWithParticipantsByPk() throws Exception {
        var result = proposalService.findWithParticipantsByPk(8425);
        assertNotNull(result);
    }

    @Test public void testFindByPkWithFetch() throws Exception {
        var result = proposalService.findByPk(8425, true, true, true);
        assertNotNull(result);
    }

    @Test public void testFindByCodeAndNumber() throws Exception {
        var result = proposalService.findByCodeAndNumber("I", "20210046", true, true, true);
        assertEquals(1, result.size());
    }

    @Test public void testUpdateProposalFromIds() throws Exception {
        var result = proposalService.updateProposalFromIds(8426, 8425);
        assertNotNull(result);
    }

    @Test public void testFindProposalByLoginName() {
        var result = proposalService.findProposalByLoginName("I20210046");
        assertEquals(1, result.size());
    }

}