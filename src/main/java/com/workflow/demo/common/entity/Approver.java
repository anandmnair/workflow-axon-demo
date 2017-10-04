package com.workflow.demo.common.entity;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Approver {

	private String teamCode;
	
	private String assignee;
	
	private Map<String, List<String>> parents;

	private Map<String, List<String>> next;

	private boolean active;
	
	private String status;
	
	private Object response;

}
