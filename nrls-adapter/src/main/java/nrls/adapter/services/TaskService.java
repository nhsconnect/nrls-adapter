package nrls.adapter.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskService {
	
	
	@Scheduled(cron="${task.schedule.cron}")
	public void extractTask() {
		
	}

}
