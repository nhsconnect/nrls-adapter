package nrls.adapter.services;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import nrls.adapter.enums.RequestType;

@Component
public class LoggingService {

	private Logger LOGGER = Logger.getLogger(LoggingService.class);
	public String consumerIdentifier = null;
	public String providerIdentifier = null;
	
	public void error(String message, RequestType type) {
		if (type.equals(RequestType.CONSUMER)) {
			LOGGER.error(consumerIdentifier + ": " + message);
		}
		if (type.equals(RequestType.PROVIDER)) {
			LOGGER.error(providerIdentifier + ": " + message);
		}
		if (type.equals(null)) {
			LOGGER.error("System: " + message);
		}
	}
	
	public void info(String message, RequestType type) {
		if (type.equals(RequestType.CONSUMER)) {
			LOGGER.info(consumerIdentifier + ": " + message);
		}
		if (type.equals(RequestType.PROVIDER)) {
			LOGGER.info(providerIdentifier + ": " + message);
		}
		if (type.equals(null)) {
			LOGGER.error("System: " + message);
		}
	}
	
}
