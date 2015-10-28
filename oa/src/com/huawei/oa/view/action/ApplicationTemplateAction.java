package com.huawei.oa.view.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.struts2.ServletActionContext;
import org.jbpm.api.ProcessDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.huawei.oa.base.ModelDrivenBaseAction;
import com.huawei.oa.domain.ApplicationTemplate;
import com.mockrunner.util.common.FileUtil;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
public class ApplicationTemplateAction extends ModelDrivenBaseAction<ApplicationTemplate> {

	private File upload;// 上传的文件，保存在临时文件夹中、
	private InputStream inputStream;// 下载用的

	// 列表
	public String list() {
		// 准备数据
		List<ApplicationTemplate> applicationTemplateList = applicationTemplateService.findAll();
		ActionContext.getContext().put("applicationTemplateList", applicationTemplateList);

		return "list";
	}

	// 删除
	public String delete() {

		// 不仅要删掉数据库中的路径，还要服务器下的文件的模板
		applicationTemplateService.delete(model.getId());
		return "toList";
	}

	// 添加页面
	public String addUI() {

		// 准备数据
		List<ProcessDefinition> processDefinitionList = processDefinitionService.findAllByLatestVersions();
		ActionContext.getContext().put("processDefinitionList", processDefinitionList);

		return "saveUI";
	}

	// 添加
	public String add() {

		String path = uploadFile(upload);

		// 设置值
		model.setPath(path);
		// 保存
		applicationTemplateService.save(model);
		return "toList";
	}

	// 修改页面
	public String editUI() {
		// 准备数据
		// 准备数据
		List<ProcessDefinition> processDefinitionList = processDefinitionService.findAllByLatestVersions();
		ActionContext.getContext().put("processDefinitionList", processDefinitionList);

		ApplicationTemplate applicationTemplate = applicationTemplateService.getById(model.getId());
		ActionContext.getContext().getValueStack().push(applicationTemplate);

		return "saveUI";
	}

	// 修改
	public String edit() {
		//取出原对象
		ApplicationTemplate applicationTemplate = applicationTemplateService.getById(model.getId());
		// 更新属性
		applicationTemplate.setName(model.getName());
		applicationTemplate.setProcessDefinitonKey(model.getProcessDefinitonKey());
		// ----如果是上传文件
		if (upload != null) {
			// >>>>删除原来的文件
			File file = new File(applicationTemplate.getPath());
			if(file.exists()){
				file.delete();
			}
			// >>>>更新为上传的文件
			model.setPath(uploadFile(upload));
		}
		// 更新到数据库中
		applicationTemplateService.update(applicationTemplate);
		return "toList";
	}

	// 下载
	public String download() throws Exception {
		// 准备下载资源
		ApplicationTemplate applicationTemplate = applicationTemplateService.getById(model.getId());
		inputStream = new FileInputStream(applicationTemplate.getPath());

		// 解决下载的保存的文件名乱码问题
		String fileName = URLEncoder.encode(applicationTemplate.getName(), "utf-8");

		// 存到map集合中，在sturt。xml中获取
		ActionContext.getContext().put("fileName", fileName);
		return "download";
	}

	// ------------------------------
	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}
