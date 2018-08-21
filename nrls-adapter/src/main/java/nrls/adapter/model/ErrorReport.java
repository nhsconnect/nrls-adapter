package nrls.adapter.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * The ErrorReport object represents a error report that will be sent if an
 * error occurs
 */
public class ErrorReport {

    private HashMap<String, List<ErrorInstance>> groupedErrors;

    public ErrorReport(List<ErrorInstance> errorInstantList) {
        groupedErrors = new HashMap<>();
        for (ErrorInstance error : errorInstantList) {
            List<ErrorInstance> errorsOfType = groupedErrors.get(error.getType());
            if (null == errorsOfType) {
                errorsOfType = new ArrayList();
                groupedErrors.put(error.getType(), errorsOfType);
            }
            errorsOfType.add(error);
        }
    }

    public String getReportAsHTML() {

        String html = "<html><head></head><body>";
        html += "<h1>NRLS Adapter - Error Report</h1>";

        html += "<div style='padding: 5px;' >The following errors occurred:</div>";

        Set errorTypes = groupedErrors.keySet();

        String errorTables = "";
        
        html += "<ul>";
        for (Object errorType : errorTypes) {
            int errorCount = 0;
            List<ErrorInstance> errorsOfType = groupedErrors.get(errorType);

            errorTables += "<h2>Details - Errors of type: " + errorType + "</h2>";
            errorTables += "<table style='border: solid 1px black; border-collapse:collapse;'>";
            errorTables += ErrorInstance.getErrorTableHTMLHeader();
            for (ErrorInstance error : errorsOfType) {
                errorTables += error.getErrorTableHTMLRow();
                errorCount++;
            }
            errorTables += "</table>";
            // Add counts of error types first
            html += "<li>Errors of type: " + errorType + " (" + errorCount + ")</li>";
        }
        html += "</ul>";

        // Add the tables html in after the counts for each type have been added
        html += errorTables;
        
        html += "</body></html>";
        return html;
    }
}
