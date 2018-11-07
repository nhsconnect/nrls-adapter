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

import java.util.ArrayList;
import java.util.List;

/**
 * The Report object represents a report that will be sent by email after a batch upload of document references
 * is performed
 */
public class Report {
        
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
    
    
    public void addCount(int total, int successful, int failed){
        countBanner = "<div class='countBanner' style='padding: 5px;' >Total processed: " + total + " ( Successful: " + successful + ", Failures: " + failed + ")</div>";
    }
    
    public String getReportAsHTML(){
        
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
