package ispyb.server.biosaxs.services.core.experiment;

import ispyb.TestBase;
import ispyb.server.biosaxs.services.core.ExperimentScope;
import jakarta.inject.Inject;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore
public class Experiment3ServiceBeanTest extends TestBase {

    @Inject private Experiment3Service service;

    @Test
    public void findById() {
        var result = service.findById(1234, ExperimentScope.PREPARE_EXPERIMENT, 8425);
        assertNull(result);
    }

    @Test public void findByMeasurementId(){
        var result = service.findByMeasurementId(1234);
        assertNull(result);
    }
}