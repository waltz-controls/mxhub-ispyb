package ispyb.server.mx.services.collections;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore
public class BeamLineSetup3ServiceBeanTest extends TestBase {

    @Inject private BeamLineSetup3Service service;

    @Test
    public void findByPk() throws Exception{
        var result = service.findByPk(1463726);
        assertNotNull(result);
    }

    @Test
    public void findByScreeningInputId() throws Exception {
        var result = service.findByScreeningInputId(1234, false);
        assertNull(result);//TODO provide test data
    }
}