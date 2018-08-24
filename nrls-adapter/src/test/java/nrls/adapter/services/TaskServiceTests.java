package nrls.adapter.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jboss.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;

import nrls.adapter.model.AuditEntity;
import nrls.adapter.model.Report;
import nrls.adapter.model.ReportDocumentReference;
import nrls.adapter.model.task.Attachment;
import nrls.adapter.model.task.Author;
import nrls.adapter.model.task.Coding;
import nrls.adapter.model.task.Content;
import nrls.adapter.model.task.Custodian;
import nrls.adapter.model.task.Subject;
import nrls.adapter.model.task.Task;
import nrls.adapter.model.task.Type;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTests {

	private Logger loggingServiceGER = Logger.getLogger(TaskServiceTests.class);

	@Mock
	RequestService requestServiceMock;
	@Mock
	Audit auditMock;

	@InjectMocks
	TaskService taskServiceImpl;

	Task task;

	@Before
	public void before() throws Exception {
		// Setup data
		task = new Task();
		// Status
		task.setStatus("Current");
		// Type
		Type type = new Type();
		Coding coding = new Coding();
		coding.setSystem("http://snomed.info/sct");
		coding.setCode("736253002");
		coding.setDisplay("Mental health crisis plan (record artifact)");
		type.setCoding(coding);
		task.setType(type);
		// Subject
		Subject subject = new Subject();
		subject.setNhsNumber("9464250321");
		task.setSubject(subject);
		// Author
		Author author = new Author();
		author.setOdsCode("RV9");
		task.setAuthor(author);
		// Custodian
		Custodian custodian = new Custodian();
		custodian.setOdsCode("RV9");
		task.setCustodian(custodian);
		// Content
		Content content = new Content();
		Attachment attachment = new Attachment();
		attachment.setContentType("text/html<");
		attachment.setUrl("http://www.humbercontactPage/?");
		attachment.setTitle("Mental health Crisis Plan Report");
		attachment.setCreation("2018-05-02");
		content.setAttchment(attachment);
		task.setContent(content);
		// Action
		task.setAction("Create");
		// PointerMasterIdentifier
		task.setPointerMasterIdentifier("c037a0cb-12kl-4976-83a1-a5d2703e6aa3");
		taskServiceImpl.currentFile = Paths.get("provider/task.xml");

	}

	// Check that the schedule will result in the task running at midnight every
	// night.
	@Test
	public void testScheduler() {
		org.springframework.scheduling.support.CronTrigger trigger = new CronTrigger("0 0 0 * * ?");

		Calendar today = Calendar.getInstance();
		today.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss EEEE");
		final Date yesterday = today.getTime();
		loggingServiceGER.info("Yesterday was : " + df.format(yesterday));
		Date nextExecutionTime = trigger.nextExecutionTime(new TriggerContext() {

			@Override
			public Date lastScheduledExecutionTime() {
				return yesterday;
			}

			@Override
			public Date lastActualExecutionTime() {
				return yesterday;
			}

			@Override
			public Date lastCompletionTime() {
				return yesterday;
			}
		});

		String message = "Next Execution date: " + df.format(nextExecutionTime);
		loggingServiceGER.info(message);

		assertEquals("2018/08/25 00:00:00 Saturday", df.format(nextExecutionTime));
	}

	// Check processing an individual task produces a valid status:
	// Mock the response from the "requestService"
	@Test
	public void testProcessTask_Create_Success() throws Exception {
		when(requestServiceMock.performPost(task)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
		when(auditMock.getAuditEntity("c037a0cb-12kl-4976-83a1-a5d2703e6aa3"))
				.thenReturn(new AuditEntity("c037a0cb-12kl-4976-83a1-a5d2703e6aa3"));
		assertEquals(true, taskServiceImpl.processTask(task, new Report(), new ReportDocumentReference(task)));
	}

	@Test
	public void testProcessTask_Delete_Success() throws Exception {
		task.setAction("Delete");
		when(requestServiceMock.performDelete(task)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
		when(auditMock.getAuditEntity("c037a0cb-12kl-4976-83a1-a5d2703e6aa3"))
				.thenReturn(new AuditEntity("c037a0cb-12kl-4976-83a1-a5d2703e6aa3"));
		assertEquals(true, taskServiceImpl.processTask(task, new Report(), new ReportDocumentReference(task)));
	}

	@Test
	public void testProcessTask_Create_Failed() throws Exception {
		when(requestServiceMock.performPost(task)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
		when(auditMock.getAuditEntity("c037a0cb-12kl-4976-83a1-a5d2703e6aa3"))
				.thenReturn(new AuditEntity("c037a0cb-12kl-4976-83a1-a5d2703e6aa3"));
		assertEquals(false, taskServiceImpl.processTask(task, new Report(), new ReportDocumentReference(task)));
	}

	@Test
	public void testProcessTask_Delete_Failed() throws Exception {
		task.setAction("Delete");
		when(requestServiceMock.performDelete(task)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		when(auditMock.getAuditEntity("c037a0cb-12kl-4976-83a1-a5d2703e6aa3"))
				.thenReturn(new AuditEntity("c037a0cb-12kl-4976-83a1-a5d2703e6aa3"));
		assertEquals(false, taskServiceImpl.processTask(task, new Report(), new ReportDocumentReference(task)));
	}

}
