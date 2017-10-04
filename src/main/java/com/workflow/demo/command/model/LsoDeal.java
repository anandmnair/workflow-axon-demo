package com.workflow.demo.command.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LsoDeal {

	private String dealId;
	
	private String dealCode;
	
	private Long amount;
	
	private Long tenure;
}
