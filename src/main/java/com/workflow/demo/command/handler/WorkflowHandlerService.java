package com.workflow.demo.command.handler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workflow.demo.command.ApproverAddedEvent;
import com.workflow.demo.command.ApproverRespondedEvent;
import com.workflow.demo.command.WorkflowCreatedEvent;
import com.workflow.demo.common.entity.Approver;
import com.workflow.demo.common.entity.Workflow;
import com.workflow.demo.common.repository.WorkflowRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WorkflowHandlerService {
	
	@Autowired
	private WorkflowRepository workflowRepository;
	
	@EventHandler
	public void handle(WorkflowCreatedEvent event) {
		Workflow workflow = Workflow.builder()
				.workflowId(event.getWorkflowId())
				.requestType(event.getRequestType())
				.request(event.getRequest())
				.version(event.getVersion())
				.initiator(event.getInitiator())
				.build();
		Workflow workflowResult = workflowRepository.save(workflow);
		log.info("Workflow created :: {}", workflowResult);
	}
	
	@EventHandler
	public void handle(ApproverAddedEvent event) {
		Workflow workflow = workflowRepository.findOne(event.getWorkflowId());
		if(workflow.getApprovers()==null) {
			workflow.setApprovers(new LinkedHashMap<>());
		}
		workflow.getApprovers().put(event.getTeamCode(), new Approver(event.getTeamCode(), event.getAssignee(), event.getParents(), event.getNext(),MapUtils.isEmpty(event.getParents())?true:false,"TODO",null));
		Workflow workflowResult = workflowRepository.save(workflow);
		log.info("Approver {} added to workflow {} :: {}", event.getTeamCode(), event.getWorkflowId(), workflowResult);
	}
	
	@EventHandler
	public void handle(ApproverRespondedEvent event) {
		Workflow workflow = workflowRepository.findOne(event.getWorkflowId());
		Approver approver = workflow.getApprovers().get(event.getTeamCode());
		approver.setStatus(event.getStatus());
		if(MapUtils.isNotEmpty(approver.getNext())) {
			List<String>nextApprovers = approver.getNext().get(event.getStatus());
			if(CollectionUtils.isNotEmpty(nextApprovers)) {
				for(String next : nextApprovers) {
					Approver nextApprover = workflow.getApprovers().get(next);
					if(checkActiveFlag(workflow,nextApprover)) {
						nextApprover.setStatus("INPROGRESS");
						nextApprover.setActive(true);
					}
				}
			}
		}
		Workflow workflowResult = workflowRepository.save(workflow);
		log.info("Approver {} added to workflow {} :: {}", event.getTeamCode(), event.getWorkflowId(), workflowResult);
	}
	
	private boolean checkActiveFlag(Workflow workflow, Approver approver) {
		if(MapUtils.isNotEmpty(approver.getParents())) {
			for(Map.Entry<String, List<String>> entry : approver.getParents().entrySet()) {
				for(String teamCode : entry.getValue()) {
					Approver parentApprover = workflow.getApprovers().get(teamCode);
					if(!entry.getKey().equals(parentApprover.getStatus())) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
}
