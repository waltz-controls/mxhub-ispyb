package ispyb.server.webservice.smis.util;

import java.util.Map;

import ispyb.server.smis.SMISWebService;
import ispyb.server.smis.SMISWebService_Service;
import jakarta.xml.ws.BindingProvider;


import ispyb.common.util.Constants;

public class SMISWebServiceGenerator {
	
	public static SMISWebService getSMISWebService()  {
		
		SMISWebService_Service sws = new SMISWebService_Service();
		SMISWebService wsPort = sws.getSMISWebServiceBeanPort();

		//SMISWebService ws=service.get
		BindingProvider bindingProvider = (BindingProvider)wsPort;
		Map requestContext = bindingProvider.getRequestContext();
		
		//usern="*****";
		//userp="*****";
		requestContext.put(BindingProvider.USERNAME_PROPERTY, Constants.getUserSmisLoginName());
		requestContext.put(BindingProvider.PASSWORD_PROPERTY, Constants.getUserSmisPassword());
		
		return wsPort;
	}

	public static SMISWebService getWs() {
		return getSMISWebService();
	}

}
