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
import nrls.adapter.model.AuditEntity;
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

        boolean isEmpty = false;
        while (!isEmpty) {
            try {
                Task task = (Task) in.readObject();
                AuditEntity auditEntity = audit.getAuditEntity(task.getPointerMasterIdentifier());
				auditEntity.setConsumerRequestData(RequestType.PROVIDER, task.getSubject().getNhsNumber(), task.getAuthor().getOdsCode(), task.getPointerMasterIdentifier(),
						xstream.toXML(task), fromAsid);
				auditEntity.setMessage(taskFileLocation + " - " + new Date().toGMTString());
                if (task.getAction().equals("Create")) {
                	auditEntity.setType(RequestType.PROVIDERCREATE);
                    System.out.println(requestService.performPost(task).getStatusCodeValue());
                } else {
                	auditEntity.setType(RequestType.PROVIDERDELETE);
                    System.out.println(requestService.performDelete(task).getStatusCodeValue());
                }
                audit.saveAuditEntity(task.getPointerMasterIdentifier());
            } catch (EOFException e) {
                isEmpty = true;
                fileHelper.closeFile();
            } catch (Exception ex) {
                // Error which is not the end of the file
                System.err.println("Error processing task: " + ex.getMessage());
                // Log error
            }
        }

        FileHelper.archiveFile(taskFileLocation);
    }

}
