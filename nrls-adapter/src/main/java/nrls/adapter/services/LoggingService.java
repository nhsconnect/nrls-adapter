/* Copyright 2018 NHS Digital

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

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
