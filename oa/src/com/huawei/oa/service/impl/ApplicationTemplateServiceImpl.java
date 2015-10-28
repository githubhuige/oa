package com.huawei.oa.service.impl;

import java.io.File;

import org.springframework.stereotype.Service;

import com.huawei.oa.base.BaseDaoImpl;
import com.huawei.oa.domain.ApplicationTemplate;
import com.huawei.oa.service.ApplicationTemplateService;

@Service
public class ApplicationTemplateServiceImpl extends BaseDaoImpl<ApplicationTemplate> implements ApplicationTemplateService {

	@Override
	public void delete(Long entityId) {

		//删除数据库中记录
		ApplicationTemplate applicationTemplate = getById(entityId);
		getSession().delete(applicationTemplate);
		//删除文件
		String filePath = applicationTemplate.getPath();
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
	
	}

	
}
