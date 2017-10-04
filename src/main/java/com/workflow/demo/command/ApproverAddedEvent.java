package com.workflow.demo.command;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproverAddedEvent {

	private String workflowId;
	
	private String teamCode;
	
	private String assignee;

	private Map<String, List<String>> parents;
	
	private Map<String, List<String>> next;
}
