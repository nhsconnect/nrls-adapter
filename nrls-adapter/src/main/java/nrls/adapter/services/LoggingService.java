package nrls.adapter.services;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import nrls.adapter.enums.RequestType;

@Component
public class LoggingService {

	private Logger loggingServiceGER = Logger.getLogger(LoggingService.class);
	public String consumerIdentifier = null;
	public String providerIdentifier = null;
	
	public void error(String message, RequestType type) {
		if (type.equals(RequestType.CONSUMER)) {
			loggingServiceGER.error(consumerIdentifier + ": " + message);
		}
		if (type.equals(RequestType.PROVIDER)) {
			loggingServiceGER.error(providerIdentifier + ": " + message);
		}
		if (type.equals(null)) {
			loggingServiceGER.error("System: " + message);
		}
	}
	
	public void info(String message, RequestType type) {
		if (type.equals(RequestType.CONSUMER)) {
			loggingServiceGER.info(consumerIdentifier + ": " + message);
		}
		if (type.equals(RequestType.PROVIDER)) {
			loggingServiceGER.info(providerIdentifier + ": " + message);
		}
		if (type.equals(null)) {
			loggingServiceGER.error("System: " + message);
		}
	}
	
}
