package nrls.adapter.services;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.XStream;

import nrls.adapter.enums.RequestType;
import nrls.adapter.helpers.FileHelper;
import nrls.adapter.model.AuditEntity;
import nrls.adapter.model.ErrorInstance;
import nrls.adapter.model.Nrls;
import nrls.adapter.model.Report;
import nrls.adapter.model.ReportDocumentReference;
import nrls.adapter.model.task.Task;

@Component
public class TaskService {

	@Autowired
	private LoggingService loggingService;

	@Autowired
	FileHelper fileHelper;

	@Autowired
	private Audit audit;
	private final XStream xstream;

	@Autowired
	private RequestService requestService;

	@Autowired
	private EmailService emailService;
	
	@Value("${provider.enabled}")
	private boolean providerEnabled;
	@Value("${task.folder.location}")
	private String tasksFolderLocation;
	@Value("${task.failed.task.location}")
	private String failedTaskFileLocation;
	@Value("${adapter.asid}")
	private String fromAsid;
	
	public Path currentFile;

	public TaskService() {
		xstream = new XStream();
	}

	// Get a stream containing all of the task files in the "provider" directory and 
	// pass each file to the "Process Taskfile method" in turn
	@Scheduled(cron = "${task.schedule.cron}")
	public void extractTaskFileList() {
		if (providerEnabled) {
			ArrayList<Path> filePathStream = fileHelper.getFileList(tasksFolderLocation);
			if (filePathStream.size() == 0) {
				Report report = new Report();
				report.addComment("No task files were found in the configured directory.");
				report.addCount(0, 0, 0);
				emailService.sendReport(report);
			} else {
				for(Path filePath : filePathStream) {
					currentFile = filePath;
					processTaskFile(fileHelper.getObjectInputStream(filePath.toString()), filePath);
				}
			}
		}
	}

	// Process a single batch of tasks and compile/send report.
	public void processTaskFile(ObjectInputStream in, Path file) {
		Task task = null;
		Report report = new Report();
		ReportDocumentReference reportDocRef = null;
		int totalCount = 0;
		int successCount = 0;
		int failCount = 0;
		boolean taskStatus = false;

		Nrls failedTasks = new Nrls();
		boolean isEmpty = false;
		while (!isEmpty) {
			try {
				task = (Task) in.readObject();
				loggingService.providerIdentifier = task.getPointerMasterIdentifier();
				loggingService.info("Processing task: " + task.getAction() + " (" + task.getPointerMasterIdentifier() + ") - " + file.getFileName(), RequestType.PROVIDER);
				reportDocRef = new ReportDocumentReference(task);
				taskStatus = processTask(task, report, reportDocRef);
			} catch (EOFException e) {
				isEmpty = true;
				fileHelper.closeFile();
				break;
			} catch (NullPointerException npex) {
				npex.printStackTrace();
				isEmpty = true;
				report.addComment("No tasks file was found in the configured directory.");
			} catch (Exception ex) {
				if (null != reportDocRef) {
					reportDocRef.setSuccess(false);
					reportDocRef.setDetails(ex.getMessage());
					report.addDocumentFailedReference(reportDocRef);
				}
				
				AuditEntity auditEntity = audit.getAuditEntity(task.getPointerMasterIdentifier());
				auditEntity.setNrlsResponse(ex.getMessage());
				audit.saveAuditEntity(task.getPointerMasterIdentifier());
				// Send out Error Email....
				ErrorInstance message = new ErrorInstance("Connection Error", ex.getMessage(),
						task.getPointerMasterIdentifier(), task.toString(), null);
				emailService.sendError(message);
				
				taskStatus = false;
				// Error which is not the end of the file
				if (task != null) {
					loggingService.error("Error processing task (" + task.getPointerMasterIdentifier() + "): " + ex.getMessage(), RequestType.PROVIDER);
				}
			}
			
			// Update task counts/failed task list.
			totalCount++;
			if (taskStatus) {
				successCount++;
			} else {
				failCount++;
				failedTasks.addTask(task);
			}
		}
		
		// Write out the failed task file.
		if (failedTasks.getTask().size() != 0) {
			FileHelper.writeObjectToFileAsXML(failedTaskFileLocation + "NrlsError_" + FileHelper.formatCurrentDate() + "-" + file.getFileName(), failedTasks);
		}
		
		FileHelper.archiveFile(file.toString());
		report.addCount(totalCount, successCount, failCount);
		emailService.sendReport(report);
	}
	
	// Process and audit a single task.
	public boolean processTask(Task task, Report report, ReportDocumentReference reportDocRef) throws Exception {
		HttpStatus status = null;
		AuditEntity auditEntity = audit.getAuditEntity(task.getPointerMasterIdentifier());
		auditEntity.setConsumerRequestData(RequestType.PROVIDER, task.getSubject().getNhsNumber(), task.getAuthor().getOdsCode(), task.getPointerMasterIdentifier(), xstream.toXML(task), fromAsid);
		auditEntity.setMessage(currentFile.getFileName() + " - " + FileHelper.formatDate(new Date()));

		if (task.getAction().equals("Create")) {
			auditEntity.setType(RequestType.PROVIDERCREATE);
			status = requestService.performPost(task).getStatusCode();
		} else if (task.getAction().equals("Delete")) {
			auditEntity.setType(RequestType.PROVIDERDELETE);
			status = requestService.performDelete(task).getStatusCode();
		}
		if (null != task) {
			audit.saveAuditEntity(task.getPointerMasterIdentifier());
		}

		if (status.equals(HttpStatus.OK) || status.equals(HttpStatus.CREATED)) {
			reportDocRef.setSuccess(true);
			report.addDocumentSuccessReference(reportDocRef);
			return true;
		} else {
			reportDocRef.setSuccess(false);
			report.addDocumentFailedReference(reportDocRef);
			return false;
		}
	}

}
