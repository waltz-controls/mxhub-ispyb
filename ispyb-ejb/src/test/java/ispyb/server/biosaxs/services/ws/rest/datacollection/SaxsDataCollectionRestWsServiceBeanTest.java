package ispyb.server.biosaxs.services.ws.rest.datacollection;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore
public class SaxsDataCollectionRestWsServiceBeanTest extends TestBase {

    @Inject private SaxsDataCollectionRestWsService service;

    @Test
    public void getDataCollectionBySessionId() {
        var result = service.getDataCollectionBySessionId(8454, 61555);
        assertNotNull(result);
    }
}