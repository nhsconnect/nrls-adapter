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

package nrls.adapter.contoller;

import com.thoughtworks.xstream.XStream;

import javax.servlet.http.HttpServletRequest;
import nrls.adapter.enums.RequestType;
import nrls.adapter.model.AuditEntity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import nrls.adapter.model.EprRequest;
import nrls.adapter.services.Audit;
import nrls.adapter.services.LoggingService;
import nrls.adapter.services.RequestService;
import nrls.adapter.services.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Controller
@RequestMapping("/api/pointers")
public class ConsumerController {

	@Autowired
	private Audit audit;

	@Autowired
	private RequestService requestService;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private LoggingService loggingService;
	
	@Value("${adapter.asid}")
	private String fromAsid;
	
	@Value("${nrls.api.requesting.organisation}")
	private String odsId;

	private final XStream xstream;

	public ConsumerController() {
		xstream = new XStream();
	}

	@GetMapping("")
	@ResponseBody
	public ResponseEntity<?> getPointers(@RequestParam(name = "sessionId", required = true) String sessionId,
			@RequestParam(name = "userId", required = true) String userId,
			@RequestParam(name = "nhsNumber", required = true) String nhsNumber,
			@RequestParam(name = "pointerType", required = false) String pointerType, HttpServletRequest request) {

		loggingService.consumerIdentifier = sessionId;
		AuditEntity auditEntity = audit.getAuditEntity(sessionId);
		auditEntity.setConsumerRequestData(RequestType.CONSUMER, nhsNumber, odsId, userId, sessionId,
				request.getMethod() + ": " + request.getRequestURL() + "?" + request.getQueryString(), fromAsid);

		ResponseEntity<?> response = requestService.performGet(new EprRequest(sessionId, userId, nhsNumber, pointerType), false);
		auditEntity.setNrlsAdapterResponse(xstream.toXML(response));

		audit.saveAuditEntity(sessionId);
		return response;
	}

	@GetMapping("/count")
	@ResponseBody
	public ResponseEntity<?> getCount(@RequestParam(name = "sessionId", required = true) String sessionId,
			@RequestParam(name = "userId", required = true) String userId,
			@RequestParam(name = "nhsNumber", required = true) String nhsNumber, HttpServletRequest request) {
		
		loggingService.consumerIdentifier = sessionId;
		AuditEntity auditEntity = audit.getAuditEntity(sessionId);
		auditEntity.setConsumerRequestData(RequestType.CONSUMER, nhsNumber, odsId, userId, sessionId,
				request.getMethod() + ": " + request.getRequestURL() + "?" + request.getQueryString(), fromAsid);

		ResponseEntity<?> response = requestService.performGet(new EprRequest(sessionId, userId, nhsNumber), true);
		auditEntity.setNrlsAdapterResponse(xstream.toXML(response));

		audit.saveAuditEntity(sessionId);
		return response;
	}

	@GetMapping("/run-batch")
	@ResponseBody
	public ResponseEntity<?> runBatch() {
		taskService.extractTaskFileList();
		ResponseEntity<?> response = new ResponseEntity<>("Started batch tasks. - Check audit log for details.", HttpStatus.OK);
		return response;
	}

}
