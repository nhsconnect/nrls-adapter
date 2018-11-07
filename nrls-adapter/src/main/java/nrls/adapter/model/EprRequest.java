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

package nrls.adapter.model;

public class EprRequest {

    private String SessionId;
    private String UserId;
    private String NHSNumber;
    private String PointerType;

    public EprRequest(String sessionId, String userId, String nHSNumber) {
        SessionId = sessionId;
        UserId = userId;
        NHSNumber = nHSNumber;
        PointerType = null;
    }

    public EprRequest(String sessionId, String userId, String nHSNumber, String pointerType) {
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
