package nrls.adapter.services;

import org.hl7.fhir.dstu3.model.DocumentReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.thoughtworks.xstream.XStream;

import nrls.adapter.helpers.HeaderGenerator;
import nrls.adapter.model.AuditEntity;
import nrls.adapter.model.EprRequest;
import nrls.adapter.model.task.Task;

@Component
@CacheConfig(cacheNames = {"pointers"})
public class RequestService {

    @Autowired
    private Audit audit;

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
    @Value("${nrls.api.pointer.system}")
    private String nrlsPointerSystem;

    @Autowired
    private RestTemplate restTemplate;

    private final XStream xstream;

    @Autowired
    private HeaderGenerator headerGenerator;

    @Autowired
    private DocumentReferenceService documentReferenceService;

    public RequestService() {
        xstream = new XStream();
    }

    // Provider Requests
    // Post by 'Document Reference'
    public ResponseEntity<?> performPost(Task task) throws Exception {
        // get audit using the 'master identifier'
        AuditEntity auditEntity = audit.getAuditEntity(task.getPointerMasterIdentifier());

        HttpEntity<String> request = new HttpEntity<>(documentReferenceService.convertTaskToDocument(task),
                headerGenerator.generateSecurityHeaders("write", "EXP001", null));

        auditEntity.setNrlsRequest(xstream.toXML(request));

        ResponseEntity<String> response = restTemplate.exchange(nrlsPostPointerUrl, HttpMethod.POST, request,
                String.class);

        auditEntity.setNrlsResponse(xstream.toXML(response));
        if (response.getStatusCode() == HttpStatus.CREATED) {
            auditEntity.setSuccess(true);
        }
        return response;
    }

    // Delete by ‘masterIdentifier’
    public ResponseEntity<?> performDelete(Task task) throws Exception {
        // get audit using the 'master identifier'
        AuditEntity auditEntity = audit.getAuditEntity(task.getPointerMasterIdentifier());

        HttpEntity<DocumentReference> request = new HttpEntity<>(
                headerGenerator.generateSecurityHeaders("write", "AMS01", null));

        auditEntity.setNrlsRequest(xstream.toXML(request));

        // [baseUrl]/DocumentReference?subject=[https://demographics.spineservices.nhs.uk/STU3/Patient/[nhsNumber]&identifier=[system]|[value]
        String url = nrlsGetPointersUrl + nrlsGetPointersUrlSubject + task.getSubject().getNhsNumber()
                + nrlsGetPointersUrlIdentifier + nrlsPointerSystem + "|" + task.getPointerMasterIdentifier();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);

        auditEntity.setNrlsResponse(xstream.toXML(response));
        if (response.getStatusCode() == HttpStatus.OK) {
            auditEntity.setSuccess(true);
        }
        return response;
    }

    // Consumer Requests
    @Cacheable(value = "pointers", key = "{ #eprRequest?.SessionId, #eprRequest?.NHSNumber, #count }")
    public ResponseEntity<String> performGet(EprRequest eprRequest, boolean count) {
        // get audit using the 'session Id'
        AuditEntity auditEntity = audit.getAuditEntity(eprRequest.getSessionId());

        HttpEntity<String> request = new HttpEntity<>(
                headerGenerator.generateSecurityHeaders("read", "AMS01", eprRequest.getUserId()));

        auditEntity.setNrlsRequest(xstream.toXML(request));

        String url = nrlsGetPointersUrl + nrlsGetPointersUrlSubject + eprRequest.getNHSNumber();
        if (count) {
            url = url + nrlsGetPointersUrlCount;
        }
        
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                request, String.class);

        auditEntity.setNrlsResponse(xstream.toXML(response));
        if (response.getStatusCode() == HttpStatus.OK) {
            auditEntity.setSuccess(true);
        }
        return response;
    }

}
