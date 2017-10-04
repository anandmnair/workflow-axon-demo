package com.workflow.demo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.ImmutableMap;
import com.workflow.demo.command.AddApproverCommand;
import com.workflow.demo.command.CreateWorkflowCommand;
import com.workflow.demo.command.model.LsoDeal;
import com.workflow.demo.common.entity.Workflow;
import com.workflow.demo.common.repository.WorkflowRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WorkflowAxonDemoApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private WorkflowRepository workflowRepository;
	
	private LsoDeal lsoDeal;
	
	private String user;
	
	@Before
	public void setUp(){
		workflowRepository.deleteAll();
		lsoDeal=new LsoDeal("101","DL101",1000L,12L);
		user="anand";
	}
	
	@Test
	public void dealWorkflowTest() {
		ResponseEntity<Void> response = restTemplate.postForEntity("/workflow-command/push", new CreateWorkflowCommand("W001","Deal",lsoDeal,0L,user), Void.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		ResponseEntity<Workflow[]> workflowResponse = restTemplate.getForEntity("/workflow-query", Workflow[].class);
		List<Workflow> workflows = Arrays.asList(workflowResponse.getBody());
		assertThat(workflows.size(), equalTo(1));
		String workflowId = workflows.get(0).getWorkflowId();
		AddApproverCommand addApproverCommand = new AddApproverCommand(workflows.get(0).getWorkflowId(),"A",null,  null,ImmutableMap.of("APPROVED",Arrays.asList("B")));
		response = restTemplate.postForEntity("/workflow-command/"+workflowId+"/approver", addApproverCommand, Void.class);
		addApproverCommand = new AddApproverCommand(workflows.get(0).getWorkflowId(),"B",null, ImmutableMap.of("APPROVED",Arrays.asList("A")),null);
		response = restTemplate.postForEntity("/workflow-command/"+workflowId+"/approver", addApproverCommand, Void.class);
		workflowResponse = restTemplate.getForEntity("/workflow-query", Workflow[].class);
		workflows = Arrays.asList(workflowResponse.getBody());
		assertThat(workflows.get(0).getApprovers().size(), equalTo(2));
	}

}
