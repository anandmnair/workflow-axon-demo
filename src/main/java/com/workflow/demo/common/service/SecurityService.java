package com.workflow.demo.common.service;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {

	public String getCurrentUserName() {
		//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return "anand.manissery";// authentication.getName();
	}
	
	public String getCurrentTeamCode() {
		//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return "A";//authentication.getName();
	}
	
}
