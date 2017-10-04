package com.workflow.demo.query.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.demo.common.entity.Workflow;
import com.workflow.demo.common.repository.WorkflowRepository;

@RestController
@RequestMapping("/workflow-query")
public class WorkflowQueryController {

	@Autowired
	private WorkflowRepository workflowRepository;
	
	@GetMapping
	public List<Workflow> getAllDeal() {
		Page<Workflow> page = workflowRepository.findAll(new PageRequest(0, 100));
		return page.getContent();
	}
	
	@GetMapping(value="/{workflowId}")
	public Workflow getDeal(@PathVariable("workflowId")String workflowId) {
		return workflowRepository.findOne(workflowId);
	}
	
}
