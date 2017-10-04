package com.workflow.demo.command;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproverRespondedEvent {

	@TargetAggregateIdentifier
	private String workflowId;
	
	private String teamCode;
	
	private String assignee;
	
	private String status;
	
	private Object response;
}
