package ispyb.server.mx.services.ws.rest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class WsServiceBean {

	public String getQueryFromResourceFile(String resourcefilePath) {
		try (InputStream inputStream = WsServiceBean.class.getResourceAsStream(resourcefilePath);
			 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			return reader.lines()
					.collect(Collectors.joining(" "))
					.replace("\n", " ")
					.replace("\r", " ")
					.replaceAll("\\s{2,}", " ")
					.trim();
		} catch (Exception e) {
			throw new IllegalStateException("Failed to read SQL query in " + resourcefilePath, e);
		}
	}
	    
	    
}
