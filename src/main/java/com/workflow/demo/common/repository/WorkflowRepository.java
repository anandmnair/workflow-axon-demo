package com.workflow.demo.common.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.workflow.demo.common.entity.Workflow;

@Repository
public interface WorkflowRepository extends ElasticsearchRepository<Workflow, String> {
	List<Workflow> findAll();
	List<Workflow> findByRequestType(String requestType);
}
