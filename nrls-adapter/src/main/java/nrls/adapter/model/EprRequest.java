package nrls.adapter.model;

public class EprRequest {
	private String SessionId;
	private String UserId;
	private String NHSNumber;
	private String PointerType;

	public EprRequest(String sessionId, String userId, String nHSNumber, String pointerType) {
		super();
		SessionId = sessionId;
		UserId = userId;
		NHSNumber = nHSNumber;
		PointerType = pointerType;
	}

	public String getSessionId() {
		return SessionId;
	}

	public void setSessionId(String sessionId) {
		SessionId = sessionId;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getNHSNumber() {
		return NHSNumber;
	}

	public void setNHSNumber(String nHSNumber) {
		NHSNumber = nHSNumber;
	}

	public String getPointerType() {
		return PointerType;
	}

	public void setPointerType(String pointerType) {
		PointerType = pointerType;
	}

}
