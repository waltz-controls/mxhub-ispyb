package ispyb.server.mx.services.sample;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore
public class Crystal3ServiceBeanTest extends TestBase {

    @Inject private Crystal3Service service;

    @Test
    public void findByProteinId() throws Exception {
        var result = service.findByProteinId(376497);
        assertNotNull(result);
    }
}