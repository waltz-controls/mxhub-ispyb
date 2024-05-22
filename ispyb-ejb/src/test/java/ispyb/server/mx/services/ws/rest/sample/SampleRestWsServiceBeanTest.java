package ispyb.server.mx.services.ws.rest.sample;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.apache.cxf.endpoint.PreexistingConduitSelector;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore
public class SampleRestWsServiceBeanTest extends TestBase {

    @Inject private SampleRestWsService service;

    @Test
    public void getSamplesBySessionId() {
        var result = service.getSamplesBySessionId(8454, 61555);
        assertNotNull(result);
    }
}