package nrls.adapter.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import nrls.adapter.helpers.ValueSetValidator;
import nrls.adapter.model.task.Attachment;
import nrls.adapter.model.task.Author;
import nrls.adapter.model.task.Coding;
import nrls.adapter.model.task.Content;
import nrls.adapter.model.task.Custodian;
import nrls.adapter.model.task.Subject;
import nrls.adapter.model.task.Task;
import nrls.adapter.model.task.Type;

@RunWith(MockitoJUnitRunner.class)
public class DocumentReferenceServiceTests {

	@Mock
	ValueSetValidator valueSetValidatorMock;

	@InjectMocks
	DocumentReferenceService docServiceImpl;

	Task task;

	@Before
	public void before() {
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
	}

	@Test
	public void testConvertTaskToDocRef() throws Exception {
		when(valueSetValidatorMock.validateCoding(task.getType().getCoding())).thenReturn(true);
		String docRef = docServiceImpl.convertTaskToDocument(task);
		System.out.println(docRef);
		assertEquals(true, docRef.contains("\"resourceType\":\"DocumentReference\""));
		assertEquals(true, docRef.contains("masterIdentifier\":{\"value\":\"c037a0cb-12kl-4976-83a1-a5d2703e6aa3\"}"));
		assertEquals(true, docRef.contains("\"status\":\"current\""));
		assertEquals(true, docRef.contains("\"type\":{\"coding\":[{\"system\":\"http://snomed.info/sct\",\"code\":\"736253002\",\"display\":\"Mental health crisis plan (record artifact)\"}]}"));
		assertEquals(true, docRef.contains("\"subject\":{\"reference\":\"https://demographics.spineservices.nhs.uk/STU3/Patient/9464250321\"}"));
		assertEquals(true, docRef.contains("\"author\":[{\"reference\":\"https://directory.spineservices.nhs.uk/STU3/Organization/RV9\"}]"));
		assertEquals(true, docRef.contains("\"custodian\":{\"reference\":\"https://directory.spineservices.nhs.uk/STU3/Organization/RV9\"}"));
		assertEquals(true, docRef.contains("\"content\":[{\"attachment\":{\"contentType\":\"text/html<\",\"url\":\"http://www.humbercontactPage/?\",\"title\":\"Mental health Crisis Plan Report\""));
	}

}
