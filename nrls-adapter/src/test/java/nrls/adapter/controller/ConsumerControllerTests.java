package nrls.adapter.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import nrls.adapter.contoller.ConsumerController;
import nrls.adapter.model.EprRequest;
import nrls.adapter.services.Audit;
import nrls.adapter.services.EmailService;
import nrls.adapter.services.LoggingService;
import nrls.adapter.services.RequestService;
import nrls.adapter.services.TaskService;

@RunWith(SpringRunner.class)
@WebMvcTest(ConsumerController.class)
public class ConsumerControllerTests {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private RequestService requestService;
	@MockBean
	private Audit audit;
	@MockBean
	private TaskService taskService;
	@MockBean
	private LoggingService loggingService;
	@MockBean
	private EmailService emailService;
	
	EprRequest eprRequest;
	
	
	@Before
	public void before() {
		eprRequest = new EprRequest("7232837238473248237482", "234234234", "9462205957");
	}
	
	// Test Count Requests
	@Test
	public void testValidPointersCountRequest_200() throws Exception {
		when(requestService.performGet(eprRequest, true)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
		
		MockHttpServletResponse response = mvc.perform(
                get("/api/pointers/count?sessionId=7232837238473248237482&userId=234234234&nhsNumber=9462205957")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
	}
	
	@Test // Bad Request
	public void testInValidPointersCountRequest_400() throws Exception {
		when(requestService.performGet(eprRequest, true)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
		
		MockHttpServletResponse response = mvc.perform(
                get("/api/pointers/count?sessionId=7232837238473248237482&userId=234234234")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
		
		assertEquals(400, response.getStatus());
	}
	
	// Test Pointers Request
	@Test
	public void testValidPointersRequest_200() throws Exception {
		when(requestService.performGet(eprRequest, false)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
		
		MockHttpServletResponse response = mvc.perform(
                get("/api/pointers?sessionId=7232837238473248237482&userId=234234234&nhsNumber=9462205957")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
	}
	
	@Test // Bad Request
	public void testInValidPointersRequest_400() throws Exception {
		when(requestService.performGet(eprRequest, false)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
		
		MockHttpServletResponse response = mvc.perform(
                get("/api/pointers?sessionId=7232837238473248237482&userId=234234234")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
		
		assertEquals(400, response.getStatus());
	}

}
