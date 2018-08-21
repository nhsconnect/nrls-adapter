package nrls.adapter.services;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.XStream;

import nrls.adapter.enums.RequestType;
import nrls.adapter.helpers.FileHelper;
import nrls.adapter.model.AuditEntity;
import nrls.adapter.model.Report;
import nrls.adapter.model.ReportDocumentReference;
import nrls.adapter.model.task.Task;

@Component
public class TaskService {

    @Autowired
    FileHelper fileHelper;
    
    @Autowired
	private Audit audit;
	private final XStream xstream;

    @Autowired
    private RequestService requestService;
    
    @Autowired
    private EmailService emailService;

    @Value("${task.file.location}")
    private String taskFileLocation;
    @Value("${adapter.asid}")
	private String fromAsid;
	
	public TaskService() {
		xstream = new XStream();
	}

    @Scheduled(cron = "${task.schedule.cron}")
    public void extractTask() throws ClassNotFoundException, IOException {

        ObjectInputStream in = fileHelper.getObjectInputStream(taskFileLocation);

        Report report = new Report();
        int totalCount = 0;
        int successCount = 0;
        int failCount = 0;
        
        boolean isEmpty = false;
        while (!isEmpty) {
            
            ReportDocumentReference reportDocRef = null;
            
            try {
                Task task = (Task) in.readObject();
                
                AuditEntity auditEntity = audit.getAuditEntity(task.getPointerMasterIdentifier());                
				auditEntity.setConsumerRequestData(RequestType.PROVIDER, task.getSubject().getNhsNumber(), task.getAuthor().getOdsCode(), task.getPointerMasterIdentifier(), xstream.toXML(task), fromAsid);
				auditEntity.setMessage(taskFileLocation + " - " + FileHelper.formatDate(new Date()));                
                totalCount++;
                reportDocRef = new ReportDocumentReference(task);
                
                if (task.getAction().equals("Create")) {
                	auditEntity.setType(RequestType.PROVIDERCREATE);
                    System.out.println(requestService.performPost(task).getStatusCodeValue());
                } else {
                	auditEntity.setType(RequestType.PROVIDERDELETE);
                    System.out.println(requestService.performDelete(task).getStatusCodeValue());
                }
                
                reportDocRef.setSuccess(true);
                successCount++;
                audit.saveAuditEntity(task.getPointerMasterIdentifier());
                
            } catch (EOFException e) {
                isEmpty = true;
                fileHelper.closeFile();
            } catch (NullPointerException npex) {
                isEmpty = true;
                report.addComment("No tasks file was found in the configured directory.");
            } catch (Exception ex) {
                if(null != reportDocRef){
                    reportDocRef.setSuccess(false);
                    reportDocRef.setDetails(ex.getMessage());
                }
                failCount++;
                // Error which is not the end of the file
                System.err.println("Error processing task: " + ex.getMessage());
                // Log error
            }
            
            if(null != reportDocRef){
                if(reportDocRef.getSuccess()){
                    report.addDocumentSuccessReference(reportDocRef);
                } else {
                    report.addDocumentFailedReference(reportDocRef);
                }
            }
            
        }

        FileHelper.archiveFile(taskFileLocation);
        
        report.addCount(totalCount, successCount, failCount);
        emailService.sendReport(report);
    }

}
