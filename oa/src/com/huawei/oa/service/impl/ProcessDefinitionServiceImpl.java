package com.huawei.oa.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessDefinitionQuery;
import org.jbpm.api.ProcessEngine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huawei.oa.service.ProcessDefinitionService;

@Service
@Transactional
@SuppressWarnings("unchecked")
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {

	@Resource
	private ProcessEngine processEngine;

	@Override
	/*
	 * 部署流程定义
	 */
	public void deploy(ZipInputStream zipInputStream) {
		processEngine.getRepositoryService()//
				.createDeployment()//
				.addResourcesFromZipInputStream(zipInputStream)//
				.deploy();
	}

	@Override
	public List<ProcessDefinition> findAllByLatestVersions() {

		// 查询出所有的流程定义,最新版本排在最后
		List<ProcessDefinition> all = processEngine.getRepositoryService()//
				.createProcessDefinitionQuery()//
				.orderAsc(ProcessDefinitionQuery.PROPERTY_VERSION)//
				.list();
		// 过滤出最新版本的流程定义显示
		Map<String, ProcessDefinition> map = new HashMap<String, ProcessDefinition>();
		for (ProcessDefinition pd : all) {
			map.put(pd.getKey(), pd);// 以流程定义的key作为关键字，不会重复，以流程定义为value
		}

		return new ArrayList<ProcessDefinition>(map.values());
	}

	@Override
	public void deleteByKey(String key) {

		// 根据key要删除所有版本的流程定义
		List<ProcessDefinition> list = processEngine.getRepositoryService()//
				.createProcessDefinitionQuery()//
				.processDefinitionKey(key)//
				.list();
		// 循环删除
		for (ProcessDefinition pd : list) {
			// 级联删除，删除的是对象
			processEngine.getRepositoryService().deleteDeploymentCascade(pd.getDeploymentId());
		}
	}

	@Override
	public InputStream getProcessImageResourceAsStream(String id) {
		// 根据id获取流程定义对象
		ProcessDefinition pd = processEngine.getRepositoryService()//
				.createProcessDefinitionQuery()//
				.processDefinitionId(id)//
				.uniqueResult();
		// 根据对应的流程定义的对象与图片的名称返回图片资源
		return processEngine.getRepositoryService().getResourceAsStream(pd.getDeploymentId(), pd.getImageResourceName());
	}

}
