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

import java.util.Date;
import nrls.adapter.helpers.FileHelper;

public class ErrorInstance {

    private String type;
    private String message;
    private String id;
    private Date date;
    private String request;
    private String response;

    public ErrorInstance(String type, String message, String id, String request, String response) {
        this.type = type;
        this.message = message;
        this.id = id;
        this.date = new Date();
        this.request = request;
        this.response = response;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public static String getErrorTableHTMLHeader(){
        String html = "<tr><th style='border: solid 1px black; padding: 5px;' >";
        html += "Message";
        html += "</th><th style='border: solid 1px black; padding: 5px;' >";
        html += "ID";
        html += "</th><th style='border: solid 1px black; padding: 5px;' >";
        html += "Date";
        html += "</th><th style='border: solid 1px black; padding: 5px;' >";
        html += "Type";
        html += "</th><th style='border: solid 1px black; padding: 5px;' >";
        html += "Request";
        html += "</th><th style='border: solid 1px black; padding: 5px;' >";
        html += "Response";
        html += "</th></tr>";
        return html;
    }
    
    public String getErrorTableHTMLRow(){
        String html = "<tr><td style='border: solid 1px black; padding: 5px;' >";
        html += message;
        html += "</td><td style='border: solid 1px black; padding: 5px;' >";
        html += id;
        html += "</td><td style='border: solid 1px black; padding: 5px;' >";
        html += FileHelper.formatDate(date);
        html += "</td><td style='border: solid 1px black; padding: 5px;' >";
        html += type;
        html += "</td><td style='border: solid 1px black; padding: 5px;' >";
        html += request;
        html += "</td><td style='border: solid 1px black; padding: 5px;' >";
        html += response;
        html += "</td></tr>";
        return html;
    }
}
