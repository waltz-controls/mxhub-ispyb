package ispyb.server.biosaxs.services.core.proposal;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore
public class SaxsProposal3ServiceBeanTest extends TestBase {

    @Inject private SaxsProposal3Service service;

    @Test
    public void findStockSolutionsByProposalId() {
        var result = service.findStockSolutionsByProposalId(8454);
        assertNotNull(result);
    }
}