package ispyb.server.mx.services.ws.rest.phasing;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
@Ignore
public class PhasingRestWsServiceBeanTest extends TestBase {

    @Inject private PhasingRestWsService service;

    @Test
    public void getPhasingViewBySessionId() {
        var result = service.getPhasingViewBySessionId(61555,8454);
        assertNotNull(result);
    }
}