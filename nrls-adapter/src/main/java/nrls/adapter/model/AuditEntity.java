package nrls.adapter.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import nrls.adapter.enums.RequestType;

public class AuditEntity {
    
    private Date dateLogged;    // Date audit was created
    private boolean success;    // Indication of the NRLS Adapter success in processing task/request
    private HttpStatus responseCode; // The Response code of the task.
    private RequestType type; // Type of request or NRLS action that was performed
    private String message;     // Generic additional information field in audit
    
    private String nhsNumber;
    private String id;              // SessionId/PointerMasterIdentifier?
    private String transactionId;   // Generated GUID
    
    private String userId;              // Id of user sent with request
    private String fromASID;            // ASID of the request sender
    
    private String nrlsAdapterRequest;  // Request payload recieved by the NRLS adapter
    private String nrlsAdapterResponse; // Response sent back to the consumer of the NRLS adapter
    private String nrlsRequest;         // Request sent by the adapter to the NRLS
    private String nrlsResponse;        // Response recieved by the adapter from the NRLS
    
    public void setConsumerRequestData(RequestType p_type, String p_nhsNumber, String p_userId, String p_sessionId, String p_nrlsAdapterRequest, String p_fromASID){
        type = p_type;
        nhsNumber = p_nhsNumber;
        userId = p_userId;
        id = p_sessionId;
        nrlsAdapterRequest = p_nrlsAdapterRequest;
        fromASID = p_fromASID;
        transactionId = UUID.randomUUID().toString();
    }

    public AuditEntity(String id){
        transactionId = id;
    }

    public Date getDateLogged() {
        return dateLogged;
    }

    public void setDateLogged(Date dateLogged) {
        this.dateLogged = dateLogged;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public HttpStatus getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(HttpStatus responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFromASID() {
        return fromASID;
    }

    public void setFromASID(String fromASID) {
        this.fromASID = fromASID;
    }

    public String getNrlsAdapterRequest() {
        return nrlsAdapterRequest;
    }

    public void setNrlsAdapterRequest(String nrlsAdapterRequest) {
        this.nrlsAdapterRequest = nrlsAdapterRequest;
    }

    public String getNrlsAdapterResponse() {
        return nrlsAdapterResponse;
    }

    public void setNrlsAdapterResponse(String nrlsAdapterResponse) {
        this.nrlsAdapterResponse = nrlsAdapterResponse;
    }

    public String getNrlsRequest() {
        return nrlsRequest;
    }

    public void setNrlsRequest(String nrlsRequest) {
        this.nrlsRequest = nrlsRequest;
    }

    public String getNrlsResponse() {
        return nrlsResponse;
    }

    public void setNrlsResponse(String nrlsResponse) {
        this.nrlsResponse = nrlsResponse;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }
    
}
