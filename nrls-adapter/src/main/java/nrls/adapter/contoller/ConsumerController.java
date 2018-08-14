package nrls.adapter.contoller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import nrls.adapter.model.EprRequest;

@Controller
@RequestMapping("/api/pointers")
public class ConsumerController {

	@GetMapping("")
	@ResponseBody
	public ResponseEntity<?> getPointers(@RequestParam(name = "sessionId", required = true) String sessionId,
			@RequestParam(name = "userId", required = true) String userId,
			@RequestParam(name = "nhsNumber", required = true) String nhsNumber,
			@RequestParam(name = "pointerType", required = false) String pointerType) {
		
		// Do something with the request
		
		return new ResponseEntity<>(new EprRequest(sessionId, userId, nhsNumber, pointerType), HttpStatus.OK);
	}

	@GetMapping("/count")
	@ResponseBody
	public ResponseEntity<?> getCount(@RequestParam(name = "sessionId", required = true) String sessionId,
			@RequestParam(name = "userId", required = true) String userId,
			@RequestParam(name = "nhsNumber", required = true) String nhsNumber) {

		// Do something with the request
		
		return new ResponseEntity<>(new EprRequest(sessionId, userId, nhsNumber), HttpStatus.OK);
	}

}
