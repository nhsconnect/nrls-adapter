package nrls.adapter.services;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.XStream;

import nrls.adapter.enums.RequestType;
import nrls.adapter.helpers.FileHelper;
import nrls.adapter.model.AuditEntity;
import nrls.adapter.model.Nrls;
import nrls.adapter.model.Report;
import nrls.adapter.model.ReportDocumentReference;
import nrls.adapter.model.task.Task;
import org.jboss.logging.Logger;

@Component
public class TaskService {

    private static final Logger LOG = Logger.getLogger(TaskService.class);

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
    @Value("${task.folder.location}")
    private String tasksFolderLocation;
    @Value("${task.failed.task.location}")
    private String failedTaskFileLocation;
    @Value("${adapter.asid}")
    private String fromAsid;

    public TaskService() {
        xstream = new XStream();
    }

    @Scheduled(cron = "${task.schedule.cron}")
    public void extractTask() throws ClassNotFoundException, IOException {
    	
    	Stream<Path> filePathStream = fileHelper.getFileList(tasksFolderLocation);
    	
    	filePathStream.forEach(filePath -> {
	        if (Files.isRegularFile(filePath)) {
	        	ObjectInputStream in = fileHelper.getObjectInputStream(filePath.toString());
	        	
		        Report report = new Report();
		        int totalCount = 0;
		        int successCount = 0;
		        int failCount = 0;
		        
		        Nrls failedTasks = new Nrls();
		        boolean isEmpty = false;
		        while (!isEmpty) {
		
		            ReportDocumentReference reportDocRef = null;
		            Task task = null;
		            HttpStatus status;
		            try {
		                task = (Task) in.readObject();
		
		                AuditEntity auditEntity = audit.getAuditEntity(task.getPointerMasterIdentifier());
		                auditEntity.setConsumerRequestData(RequestType.PROVIDER, task.getSubject().getNhsNumber(), task.getAuthor().getOdsCode(), task.getPointerMasterIdentifier(), xstream.toXML(task), fromAsid);
		                auditEntity.setMessage(taskFileLocation + " - " + FileHelper.formatDate(new Date()));
		                totalCount++;
		                reportDocRef = new ReportDocumentReference(task);
		
		                if (task.getAction().equals("Create")) {
		                    auditEntity.setType(RequestType.PROVIDERCREATE);
		                    status = requestService.performPost(task).getStatusCode();
		                } else {
		                    auditEntity.setType(RequestType.PROVIDERDELETE);
		                    status = requestService.performDelete(task).getStatusCode();
		                }
		
		                if (status.equals(HttpStatus.OK) || status.equals(HttpStatus.CREATED)) {
		                	reportDocRef.setSuccess(true);
		                } else {
		                	reportDocRef.setSuccess(false);
		                	failedTasks.addTask(task);
		                }
		                successCount++;
		
		            } catch (EOFException e) {
		                isEmpty = true;
		                if (failedTasks.getTask().size() != 0) {
		                	FileHelper.writeObjectToFileAsXML(failedTaskFileLocation + "tasks_failed_" + FileHelper.formatCurrentDate() + "-" + filePath.getFileName(), failedTasks);
		                }
		                fileHelper.closeFile();
		            } catch (NullPointerException npex) {
		                isEmpty = true;
		                report.addComment("No tasks file was found in the configured directory.");
		            } catch (Exception ex) {
		                if (null != reportDocRef) {
		                    reportDocRef.setSuccess(false);
		                    reportDocRef.setDetails(ex.getMessage());
		                }
		                failCount++;
		                failedTasks.addTask(task);
		                // Error which is not the end of the file
		                if (null != task) LOG.error("Error processing task (" + task.getPointerMasterIdentifier() + "): " + ex.getMessage());
		                else LOG.error("Error processing task: " + ex.getMessage());
		            }
		
		            if (null != reportDocRef) {
		                if (reportDocRef.getSuccess()) {
		                    report.addDocumentSuccessReference(reportDocRef);
		                } else {
		                    report.addDocumentFailedReference(reportDocRef);
		                }
		            }
		
		            if (null != task) {
		                audit.saveAuditEntity(task.getPointerMasterIdentifier());
		            }
		
		        }
		
		        FileHelper.archiveFile(taskFileLocation);
		
		        report.addCount(totalCount, successCount, failCount);
		        emailService.sendReport(report);
	        }
	    });
    }

}
