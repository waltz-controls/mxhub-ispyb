package ispyb.server.biosaxs.services.core.analysis;

import ispyb.TestBase;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore
public class Analysis3ServiceBeanTest extends TestBase {

    @Inject
    private Analysis3Service service;

    @Test(expected = jakarta.ejb.EJBException.class)
    public void getDataCollection() {
        var result = service.getDataCollection(123);
    }

    @Test
    public void getAllAnalysisInformation(){
        var result = service.getAllAnalysisInformation();
        assertNotNull(result);
    }

    @Test public void getCompactAnalysisByMacromoleculeId(){
        var result = service.getCompactAnalysisByMacromoleculeId(8425, 1234);
        assertNotNull(result);
    }
}