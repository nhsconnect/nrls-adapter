package nrls.adapter.contoller;

import com.thoughtworks.xstream.XStream;

import java.io.IOException;

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
import nrls.adapter.services.RequestService;
import nrls.adapter.services.TaskService;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping("/api/pointers")
public class ConsumerController {

	@Autowired
	private Audit audit;

	@Autowired
	private RequestService requestService;

	@Autowired
	private TaskService taskService;

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

		AuditEntity auditEntity = audit.getAuditEntity(sessionId);
		auditEntity.setConsumerRequestData(RequestType.CONSUMER, nhsNumber, userId, sessionId,
				request.getRequestURL() + "?" + request.getQueryString());

		ResponseEntity<?> response = requestService.performGet(new EprRequest(sessionId, userId, nhsNumber, pointerType),
				false);
		auditEntity.setNrlsAdapterResponse(xstream.toXML(response));

		audit.saveAuditEntity(sessionId);
		return response;
	}

	@GetMapping("/count")
	@ResponseBody
	public ResponseEntity<?> getCount(@RequestParam(name = "sessionId", required = true) String sessionId,
			@RequestParam(name = "userId", required = true) String userId,
			@RequestParam(name = "nhsNumber", required = true) String nhsNumber, HttpServletRequest request) {

		AuditEntity auditEntity = audit.getAuditEntity(sessionId);
		auditEntity.setConsumerRequestData(RequestType.CONSUMER, nhsNumber, userId, sessionId,
				request.getRequestURL() + "?" + request.getQueryString());

		ResponseEntity<?> response = requestService.performGet(new EprRequest(sessionId, userId, nhsNumber), true);
		auditEntity.setNrlsAdapterResponse(xstream.toXML(response));

		audit.saveAuditEntity(sessionId);
		return response;
	}

	@GetMapping("/testTasks")
	@ResponseBody
	public ResponseEntity<?> getTestTask() {

		try {
			taskService.extractTask();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ResponseEntity<?> response = new ResponseEntity<>("testing task extraction", HttpStatus.OK);
		return response;
	}

}
