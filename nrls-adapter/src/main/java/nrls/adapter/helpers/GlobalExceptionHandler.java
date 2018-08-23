package nrls.adapter.helpers;

import java.io.IOException;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import nrls.adapter.enums.RequestType;
import nrls.adapter.model.AuditEntity;
import nrls.adapter.model.ErrorInstance;
import nrls.adapter.services.Audit;
import nrls.adapter.services.EmailService;
import nrls.adapter.services.LoggingService;

// Catch generic errors globally and produce a report/error email.
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private Audit audit;

	@Autowired
	private EmailService emailService;

	@Autowired
	private LoggingService loggingService;

	// Deal with Generic Exceptions.
	@ExceptionHandler(Exception.class)
	public Exception handleDefaultException(Exception ex) {
		// Deal with error
		loggingService.error("Exception: Generic", RequestType.CONSUMER);
		loggingService.error(ex.getMessage(), RequestType.CONSUMER);
		ex.printStackTrace();
		// Send out Error Email....
		ErrorInstance message = new ErrorInstance("Generic Error", ex.getMessage(), null, null, null);
		emailService.sendError(message);
		return ex;
	}

	// Deal with I/O Exceptions.
	@ExceptionHandler(IOException.class)
	public IOException handleIOException(IOException ex) {
		// Deal with error
		loggingService.error("IOException: FileRead/Write", RequestType.CONSUMER);
		loggingService.error(ex.getMessage(), RequestType.CONSUMER);
		// Send out Error Email....
		ErrorInstance message = new ErrorInstance("IOException Error", ex.getMessage(), null, null, null);
		emailService.sendError(message);
		return ex;
	}

	// Deal with RestTemplate Exceptions.
	@ExceptionHandler(RestClientException.class)
	public RestClientException handleRestClientException(RestClientException ex, WebRequest request) {
		// Deal with error
		loggingService.error("RestClientException: Connection refused: connect", RequestType.CONSUMER);
		loggingService.error(ex.getMessage() + request, RequestType.CONSUMER);
		AuditEntity auditEntity = audit.getAuditEntity(request.getParameter("sessionId"));
		auditEntity.setNrlsResponse(ex.getMessage());
		audit.saveAuditEntity(request.getParameter("sessionId"));
		// Send out Error Email....
		ErrorInstance message = new ErrorInstance("Connection Error", ex.getMessage(),
				request.getParameter("sessionId"), formatRequest(request), null);
		emailService.sendError(message);
		return ex;
	}

	// Format request for report.
	private String formatRequest(WebRequest request) {
		String params = request.toString() + "<br>";
		Iterator<String> it = request.getParameterNames();
		while (it.hasNext()) {
			String paramKey = (String) it.next();
			params = params + paramKey + " = " + request.getParameter(paramKey) + "<br>";
		}
		return params;
	}

}
