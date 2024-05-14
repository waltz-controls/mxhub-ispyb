package ispyb.server.mx.services.ws.rest.energyscan;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore("requires db with data")
public class EnergyScanRestWsServiceTest extends TestBase {

    @Inject private EnergyScanRestWsService service;


    @Test
    public void getViewBySessionId() {
        var result = service.getViewBySessionId(8425, 1234);
        assertNotNull(result);
    }

    @Test
    public void getViewById() {
        var result = service.getViewById(8425, 1234);
        assertNotNull(result);
    }
}