package nrls.adapter.services;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import nrls.adapter.model.Task;

@Component
public class TaskService {

	@Autowired
	FileHelper fileHelper;

	@Value("${task.file.location}")
	private String taskFileLocation;

	@Scheduled(cron = "${task.schedule.cron}")
	public void extractTask() throws ClassNotFoundException, IOException {

		ObjectInputStream in = fileHelper.getObjectInputStream(taskFileLocation);

		boolean isEmpty = false;
		while (!isEmpty) {
			try {
				Task task = (Task) in.readObject();
				System.out.println(task);
			} catch (EOFException e) {
				isEmpty = true;
				fileHelper.closeFile();
			}
		}

        FileHelper.archiveFile(taskFileLocation);
	}

}
