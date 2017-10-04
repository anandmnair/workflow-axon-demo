package com.workflow.demo.domain;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.workflow.demo.command.AddApproverCommand;
import com.workflow.demo.command.ApproverAddedEvent;
import com.workflow.demo.command.ApproverRespondedEvent;
import com.workflow.demo.command.ApproverResponseCommand;
import com.workflow.demo.command.CreateWorkflowCommand;
import com.workflow.demo.command.WorkflowCreatedEvent;
import com.workflow.demo.common.entity.Approver;

import lombok.NoArgsConstructor;

@Aggregate
@NoArgsConstructor
public class WorkflowAggregator {

	@AggregateIdentifier
	private String workflowId;
	
	private Map<String,Approver> approvers = new LinkedHashMap<>();
	
	@CommandHandler
	public WorkflowAggregator(CreateWorkflowCommand command) {
		apply(new WorkflowCreatedEvent(command.getWorkflowId(), command.getRequestType(), command.getRequest(), command.getVersion(), command.getInitiator()));
	}
	
	@CommandHandler
	public void handle(AddApproverCommand command) {
		apply(new ApproverAddedEvent(command.getWorlflowId(), command.getTeamCode(), command.getAssignee(), command.getParents(), command.getNext()));
	}
	
	@CommandHandler
	public void handle(ApproverResponseCommand command) {
		apply(new ApproverRespondedEvent(command.getWorkflowId(), command.getTeamCode(), command.getAssignee(), command.getStatus(), command.getResponse()));
	}
	
	@EventSourcingHandler
	public void on(WorkflowCreatedEvent event) {
		this.workflowId = event.getWorkflowId();
	}
	
	@EventSourcingHandler
	public void on(ApproverAddedEvent event) {
		this.approvers.put(event.getTeamCode(), new Approver(event.getTeamCode(), event.getAssignee(), event.getParents(), event.getNext(),MapUtils.isEmpty(event.getParents())?true:false,"TODO",null));
	}
	
	@EventSourcingHandler
	public void on(ApproverRespondedEvent event) {
		//this.approvers.put(event.getTeamCode(), new Approver(event.getTeamCode(), event.getAssignee(), event.getParents(), event.getNext(),MapUtils.isEmpty(event.getParents())?true:false));
	}
	

}
