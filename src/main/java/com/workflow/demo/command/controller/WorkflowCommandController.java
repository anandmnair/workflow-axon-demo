package com.workflow.demo.command.controller;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.demo.command.AddApproverCommand;
import com.workflow.demo.command.ApproverResponseCommand;
import com.workflow.demo.command.CreateWorkflowCommand;
import com.workflow.demo.common.entity.Approver;
import com.workflow.demo.common.service.SecurityService;

@RestController
@RequestMapping("/workflow-command")
public class WorkflowCommandController {

	@Autowired
	private CommandGateway commandGateway;
	
	@Autowired
	private SecurityService securityService;
	
	@PostMapping("/push")
	public void pushLsoDeal(@RequestBody CreateWorkflowCommand workflowCommand) {
		commandGateway.send(new CreateWorkflowCommand(UUID.randomUUID().toString(), workflowCommand.getRequestType(), workflowCommand.getRequest(), workflowCommand.getVersion(), securityService.getCurrentUserName()));
	}
	
	@PostMapping("/{workflowId}/approver")
	public void addApprover(@PathVariable("workflowId")String workflowId, @RequestBody Approver approver) {
		commandGateway.send(new AddApproverCommand(workflowId, approver.getTeamCode(), approver.getAssignee(), approver.getParents(),approver.getNext()));
	}
	
	@PostMapping("/{workflowId}/approver/{teamCode}/response")
	public void approve(@PathVariable("workflowId")String workflowId,@PathVariable("teamCode")String teamCode, @RequestBody ApproverResponseCommand command) {
		commandGateway.send(new ApproverResponseCommand(workflowId, teamCode, command.getAssignee(), command.getStatus(), command.getResponse()));
	}
}
