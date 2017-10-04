package com.workflow.demo.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkflowCommand {
	
	private String workflowId;
	
	private String requestType;

	private Object request;
	
	private Long version;
	
	private String initiator;
}
