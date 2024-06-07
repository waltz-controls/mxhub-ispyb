package ispyb.server.smis;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebEndpoint;
import jakarta.xml.ws.WebServiceClient;
import jakarta.xml.ws.WebServiceFeature;

@WebServiceClient(
        name = "SMISWebService",
        targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
        wsdlLocation = "https://smis.esrf.fr/SMISServer-ejb3/SMISWebService/SMISWebServiceBean?wsdl"
)
public class SMISWebService_Service extends Service {
    private static final URL SMISWEBSERVICE_WSDL_LOCATION;
    private static final Logger logger = Logger.getLogger(generated.ws.smis.SMISWebService_Service.class.getName());

    public SMISWebService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SMISWebService_Service() {
        super(SMISWEBSERVICE_WSDL_LOCATION, new QName("http://webservice.service.ejb3.smis.esrf.fr/", "SMISWebService"));
    }

    @WebEndpoint(
            name = "SMISWebServiceBeanPort"
    )
    public SMISWebService getSMISWebServiceBeanPort() {
        return (SMISWebService)super.getPort(new QName("http://webservice.service.ejb3.smis.esrf.fr/", "SMISWebServiceBeanPort"), SMISWebService.class);
    }

    @WebEndpoint(
            name = "SMISWebServiceBeanPort"
    )
    public SMISWebService getSMISWebServiceBeanPort(WebServiceFeature... features) {
        return (SMISWebService)super.getPort(new QName("http://webservice.service.ejb3.smis.esrf.fr/", "SMISWebServiceBeanPort"), SMISWebService.class, features);
    }

    static {
        URL url = null;

        try {
            URL baseUrl = generated.ws.smis.SMISWebService_Service.class.getResource(".");
            url = new URL(baseUrl, "https://smis.esrf.fr/SMISServer-ejb3/SMISWebService/SMISWebServiceBean?wsdl");
        } catch (MalformedURLException var2) {
            logger.warning("Failed to create URL for the wsdl Location: 'https://smis.esrf.fr/SMISServer-ejb3/SMISWebService/SMISWebServiceBean?wsdl', retrying as a local file");
            logger.warning(var2.getMessage());
        }

        SMISWEBSERVICE_WSDL_LOCATION = url;
    }
}

