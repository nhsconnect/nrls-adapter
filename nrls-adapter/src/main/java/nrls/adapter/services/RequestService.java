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

import java.util.Date;

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
    
    @Value("${nrls.api.requesting.organisation}")
    private String requestingOrganisation;

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
                headerGenerator.generateSecurityHeaders("write", requestingOrganisation, null));

        auditEntity.setNrlsRequest("<url>" + nrlsPostPointerUrl + "</url>" + xstream.toXML(request));
        
        // [baseUrl]/DocumentReference
        ResponseEntity<String> response = restTemplate.exchange(nrlsPostPointerUrl, HttpMethod.POST, request,
                String.class);

        auditEntity.setNrlsResponse(xstream.toXML(response));
        auditEntity.setNrlsResponseTimeDate(new Date().toGMTString());
        auditEntity.setResponseCode(response.getStatusCode());
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
                headerGenerator.generateSecurityHeaders("write", requestingOrganisation, null));

        // [baseUrl]/DocumentReference?subject=[https://demographics.spineservices.nhs.uk/STU3/Patient/[nhsNumber]&identifier=[system]|[value]
        String url = nrlsGetPointersUrl + nrlsGetPointersUrlSubject + task.getSubject().getNhsNumber()
                + nrlsGetPointersUrlIdentifier + nrlsPointerSystem + "|" + task.getPointerMasterIdentifier();
        
        auditEntity.setNrlsRequest("<url>" + url + "</url>" + xstream.toXML(request));
        
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);

        auditEntity.setNrlsResponse(xstream.toXML(response));
        auditEntity.setNrlsResponseTimeDate(new Date().toGMTString());
        auditEntity.setResponseCode(response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK) {
            auditEntity.setSuccess(true);
        }
        return response;
    }

    // Consumer Requests
    @Cacheable(value = "pointers", key = "{ #eprRequest?.SessionId, #eprRequest?.NHSNumber }", condition="#count==true")
    public ResponseEntity<String> performGet(EprRequest eprRequest, boolean count) {
        // get audit using the 'session Id'
        AuditEntity auditEntity = audit.getAuditEntity(eprRequest.getSessionId());

        HttpEntity<String> request = new HttpEntity<>(
                headerGenerator.generateSecurityHeaders("read", requestingOrganisation, eprRequest.getUserId()));
        
        // [baseUrl]/DocumentReference?subject=[https://demographics.spineservices.nhs.uk/STU3/Patient/[nhsNumber]
        String url = nrlsGetPointersUrl + nrlsGetPointersUrlSubject + eprRequest.getNHSNumber();
        // [baseUrl]/DocumentReference?subject=[https://demographics.spineservices.nhs.uk/STU3/Patient/[nhsNumber]&_summary=count
        if (count) {
            url = url + nrlsGetPointersUrlCount;
        }
        
        auditEntity.setNrlsRequest("<url>" + url + "</url>" + xstream.toXML(request));
        
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                request, String.class);

        auditEntity.setNrlsResponse(xstream.toXML(response));
        auditEntity.setNrlsResponseTimeDate(new Date().toGMTString());
        auditEntity.setResponseCode(response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK) {
            auditEntity.setSuccess(true);
        }
        return response;
    }

}
