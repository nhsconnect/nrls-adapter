package nrls.adapter.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Report object represents a report that will be sent by email after a batch upload of document references
 * is performed
 */
public class Report {
    
    private Date startTime; // When the report was started
    private Date endTime;   // When the report was completed, just before sending
    
    private String countBanner;
    private String description; // Additional information about the run
    private List<String> comments; // For additional comments on report
    
    // List of document references uploaded
    List<ReportDocumentReference> documentReferencesSuccess;
    List<ReportDocumentReference> documentReferencesFail;
    
    public Report(){
        documentReferencesSuccess = new ArrayList<>();
        documentReferencesFail = new ArrayList<>();
        comments = new ArrayList<>();
        startTime = new Date();
        description = "";
        countBanner = "";
    }
    
    
    public void addDocumentSuccessReference(ReportDocumentReference docReference){
        documentReferencesSuccess.add(docReference);
    }
    
    public void addDocumentFailedReference(ReportDocumentReference docReference){
        documentReferencesFail.add(docReference);
    }
    
    public void setDescription(String desc){
        description = desc;
    }
    
    public void addComment(String comment){
        comments.add(comment);
    }
    
    public void endReport(){
        endTime = new Date();
    }
    
    public void addCount(int total, int successful, int failed){
        countBanner = "<div class='countBanner' style='padding: 5px;' >Total processed: " + total + " ( Successful: " + successful + ", Failures: " + failed + ")</div>";
    }
    
    public String getReportAsHTML(){
        
        if(null == endTime){
            endReport();
        }
        
        String html = "<html><head></head><body>";
        html += "<h1>NRLS Adapter - Report</h1>";
        html += "<div class='component description' style='padding: 5px;' >" + description + "</div>";
        
        for(String comment : comments){
            html += "<div class='component comment' style='padding: 5px;' >" + comment + "</div>";
        }
        
        html += countBanner;
        
        
        html += "<h2>Failed document reference changes on NRLS</h2>";
        
        if(documentReferencesFail.size() <= 0){
            html += "<div class='component content' style='padding: 5px;' >No attempted changes to the NRLS failed.</div>";
        } else {
            html += "<table style='border: solid 1px black; border-collapse:collapse;'>";
            html += ReportDocumentReference.getHTMLTableHeader();
            for(ReportDocumentReference docRef : documentReferencesFail){
                html += docRef.getAsHTMLTableRow();
            }
            html += "</table>";
        }
        
        
        html += "<h2>Successful document reference changes on NRLS</h2>";
        
        if(documentReferencesSuccess.size() <= 0){
            html += "<div class='component content' style='padding: 5px;' >No attempted changes to the NRLS were successful.</div>";
        } else {
            html += "<table style='border: solid 1px black; border-collapse:collapse;'>";
            html += ReportDocumentReference.getHTMLTableHeader();
            for(ReportDocumentReference docRef : documentReferencesSuccess){
                html += docRef.getAsHTMLTableRow();
            }
            html += "</table>";
        }
        
        
        html += "</body></html>";
        return html;
    }
}
