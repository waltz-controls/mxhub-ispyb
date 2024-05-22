package ispyb.server.mx.services.ws.rest.dewar;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.Test;

import static org.junit.Assert.*;

public class DewarRestWsServiceBeanTest extends TestBase {

    @Inject private DewarRestWsService service;

    @Test
    public void getDewarViewBySessionId() {
        var result = service.getDewarViewBySessionId(61585,8425);
        assertNotNull(result);
    }

    @Test
    public void getDewarViewByProposalId() {
        var result = service.getDewarViewByProposalId(8425);
        assertNotNull(result);
    }
}