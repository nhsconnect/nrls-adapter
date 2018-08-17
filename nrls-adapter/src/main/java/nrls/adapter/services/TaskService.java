package nrls.adapter.services;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import nrls.adapter.model.task.Task;

@Component
public class TaskService {

    @Autowired
    FileHelper fileHelper;

    @Autowired
    private RequestService requestService;

    @Value("${task.file.location}")
    private String taskFileLocation;

    @Scheduled(cron = "${task.schedule.cron}")
    public void extractTask() throws ClassNotFoundException, IOException {

        ObjectInputStream in = fileHelper.getObjectInputStream(taskFileLocation);

        boolean isEmpty = false;
        while (!isEmpty) {
            try {
                Task task = (Task) in.readObject();
                if (task.getAction().equals("Create")) {
                    System.out.println(requestService.performPost(task).getStatusCodeValue());
                } else {
                    System.out.println(requestService.performDelete(task).getStatusCodeValue());
                }
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
