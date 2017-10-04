package com.workflow.demo.domain;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import com.workflow.demo.command.AddApproverCommand;
import com.workflow.demo.command.ApproverAddedEvent;
import com.workflow.demo.command.ApproverRespondedEvent;
import com.workflow.demo.command.ApproverResponseCommand;
import com.workflow.demo.command.CreateWorkflowCommand;
import com.workflow.demo.command.WorkflowCreatedEvent;
import com.workflow.demo.command.model.LsoDeal;
import com.workflow.demo.domain.WorkflowAggregator;

public class WorkflowAggregatorTest {

	private FixtureConfiguration<WorkflowAggregator> fixture;
	
	private LsoDeal lsoDeal;

	@Before
	public void setUp(){
		lsoDeal=new LsoDeal(); 
		fixture=new AggregateTestFixture<>(WorkflowAggregator.class);
	}
	
	@Test
	public void createDealWorkflowTest(){
		fixture.givenNoPriorActivity()
			.when(new CreateWorkflowCommand("DL1001", "Deal", lsoDeal, 1L, "anand.manissery"))
			.expectEvents(new WorkflowCreatedEvent("DL1001", "Deal", lsoDeal, 1L, "anand.manissery"))
			;
	}
	
	@Test
	public void addApproverTest(){
		Map<String,List<String>>parents = new LinkedHashMap<>();
		parents.put("APPROVED", Arrays.asList("A"));
		Map<String,List<String>>next = new LinkedHashMap<>();
		parents.put("APPROVED", Arrays.asList("C"));
		parents.put("REJECTED", Arrays.asList("D"));

		fixture.given(new WorkflowCreatedEvent("DL1001", "Deal", lsoDeal, 1L, "anand.manissery"))
			.when(new AddApproverCommand("DL1001","B","anand.manissery",parents, next))
			.expectEvents(new ApproverAddedEvent("DL1001","B","anand.manissery",parents, next))
			;
	}
	
	@Test
	public void approverResponedTest(){
		fixture.given(new WorkflowCreatedEvent("DL1001", "Deal", lsoDeal, 1L, "anand.manissery"),
				new ApproverAddedEvent("DL1001","A","anand.manissery",null, null))
			.when(new ApproverResponseCommand("DL1001","B","anand.manissery","APPROVED",null))
			.expectEvents(new ApproverRespondedEvent("DL1001","B","anand.manissery","APPROVED",null))
			;
	}
	
}
