package nrls.adapter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import nrls.adapter.model.EprRequest;
import nrls.adapter.model.documentreference.DocumentReference;
import nrls.adapter.model.task.Task;

@Component
public class RequestService {

	// NRLS Consumer configuration
	@Value("${nrls.api.get.pointer.url}")
	private String nrlsGetPointersUrl;
	@Value("${nrls.api.get.pointer.url.subject}")
	private String nrlsGetPointersUrlSubject;
	@Value("${nrls.api.get.pointer.url.count}")
	private String nrlsGetPointersUrlCount;
	@Value("${nrls.api.get.pointer.url.identifier}")
	private String nrlsGetPointersUrlIdentifier;

	// NRLS Provider Post configuration
	@Value("${nrls.api.post.pointer.url}")
	private String nrlsPostPointerUrl;

	// NRLS Provider Delete configuration
	@Value("${nrls.api.delete.pointer.url}")
	private String nrlsDeletePointersUrl;
	@Value("${nrls.api.delete.pointer.system}")
	private String nrlsDeletePointersSystem;

	private RestTemplate restTemplate;

	@Autowired
	private HeaderGenerator headerGenerator;

	@Autowired
	public RequestService(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	// Provider Requests
	public ResponseEntity<?> performPost(Task task) {
		System.out.println(task);
		System.out.println(new DocumentReference().convertTaskToDocumentReference(task));
		HttpEntity<DocumentReference> request = new HttpEntity<>(
				new DocumentReference().convertTaskToDocumentReference(task), headerGenerator.generateSecurityHeaders("write", "EXP001", null));
		ResponseEntity<String> response = restTemplate.exchange(nrlsPostPointerUrl, HttpMethod.POST, request,
				String.class);
		return response;
	}

	// Delete by ‘masterIdentifier’
	public ResponseEntity<?> performDelete(Task task) {
		System.out.println(task);
		System.out.println(new DocumentReference().convertTaskToDocumentReference(task));
		HttpEntity<DocumentReference> request = new HttpEntity<>(headerGenerator.generateSecurityHeaders("write", "AMS01", null));
		// [baseUrl]/DocumentReference?subject=[https://demographics.spineservices.nhs.uk/STU3/Patient/[nhsNumber]&identifier=[system]|[value]
		String url = nrlsGetPointersUrl + nrlsGetPointersUrlSubject + task.getSubject().getNhsNumber()
				+ nrlsGetPointersUrlIdentifier + nrlsDeletePointersSystem + task.getPointerMasterIdentifier();
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
		return response;
	}

	// Consumer Requests
	public ResponseEntity<String> performGet(EprRequest eprRequest, boolean count) {
		HttpEntity<String> request = new HttpEntity<>(headerGenerator.generateSecurityHeaders("read", "AMS01", eprRequest.getUserId()));
		String url = nrlsGetPointersUrl + nrlsGetPointersUrlSubject + eprRequest.getNHSNumber();
		if (count) {
			url = url + nrlsGetPointersUrlCount;
		}
		ResponseEntity<String> response = restTemplate.exchange(url + eprRequest.getNHSNumber(), HttpMethod.GET,
				request, String.class);
		return response;

	}

}
