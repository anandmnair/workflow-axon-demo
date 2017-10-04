package com.workflow.demo.common.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName="workflow", type="workflow")
public class Workflow {

	@Id
	private String workflowId;
	
	private String requestType;
	
	private Object request;
	
	private Long version;

	private String initiator;
	
	private Map<String,Approver> approvers = new LinkedHashMap<>();

}
