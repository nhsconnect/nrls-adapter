package nrls.adapter.model;

import java.util.Date;
import nrls.adapter.model.task.Task;

public class ReportDocumentReference {

    private String masterIdentifier;
    private String nhsNumber;
    
    private String action;
    private String documentType;
    
    private Boolean success;
    private String details;
    private Date startTime;
    private Date endTime;
    
    public ReportDocumentReference(Task task){
        masterIdentifier = task.getPointerMasterIdentifier();
        nhsNumber = task.getSubject().getNhsNumber();
        action = task.getAction();
        documentType = task.getType().getCoding().getDisplay();
        startTime = new Date();
        success = false;
    }

    public String getMasterIdentifier() {
        return masterIdentifier;
    }

    public void setMasterIdentifier(String masterIdentifier) {
        this.masterIdentifier = masterIdentifier;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.endTime = new Date();
        this.success = success;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
   
    public String getAsHTMLTableRow(){
        String html = "<tr><td style='border: solid 1px black; padding: 5px;' >";
        html += masterIdentifier;
        html += "</td><td style='border: solid 1px black; padding: 5px;' >";
        html += nhsNumber;
        html += "</td><td style='border: solid 1px black; padding: 5px;' >";
        html += action;
        html += "</td><td style='border: solid 1px black; padding: 5px;' >";
        html += documentType;
        if(success){
            html += "</td><td class='success' style='border: solid 1px black; padding: 5px;' >Successful";
        } else {
            html += "</td><td class='fail' style='border: solid 1px black; padding: 5px;' >Failed";
        }
        html += "</td><td style='border: solid 1px black; padding: 5px;' >";
        html += details;
        html += "</td><td style='border: solid 1px black; padding: 5px;' >";
        html += startTime;
        html += "</td><td style='border: solid 1px black; padding: 5px;' >";
        html += endTime;
        html += "</td></tr>";
        return html;
    }
    
    public static String getHTMLTableHeader(){
        String html = "<tr><th style='border: solid 1px black; padding: 5px;' >";
        html += "Master Identifier";
        html += "</th><th style='border: solid 1px black; padding: 5px;' >";
        html += "NHS Number";
        html += "</th><th style='border: solid 1px black; padding: 5px;' >";
        html += "Action";
        html += "</th><th style='border: solid 1px black; padding: 5px;' >";
        html += "Document Type";
        html += "</th><th style='border: solid 1px black; padding: 5px;' >";
        html += "Success";
        html += "</th><th style='border: solid 1px black; padding: 5px;' >";
        html += "Details";
        html += "</th><th style='border: solid 1px black; padding: 5px;' >";
        html += "Start Time";
        html += "</th><th style='border: solid 1px black; padding: 5px;' >";
        html += "End Time";
        html += "</th></tr>";
        return html;
    }
}
