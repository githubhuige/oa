package com.huawei.oa.view.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.jbpm.api.ProcessDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.huawei.oa.base.BaseAction;
import com.opensymphony.xwork2.ActionContext;

/**
 * extends BaseAction; 之所以继承basseAction是因为ModelDrivenBaseAction中的需要传对象，而流程没有对象，
 * 但要使用分页的页面页数等信息，所以重载一个 action,直接跳过ModelDrivenBaseAction，获取BaseAction中信息
 * 
 * @author Administrator
 * 
 */
@Controller
@Scope("prototype")
public class ProcessDefinitionAction extends BaseAction {

	private String id;// 查看哪个流程图
	private String key;// 删除的关键字
	private File upload; // 上传文件，存放在临时文件中
	private InputStream inputStream;// 下载用的，在struts2.xml中作为关键字传过去

	/**
	 * 列表
	 * 
	 * @return
	 */
	public String list() {
		List<ProcessDefinition> processDefinitionList = processDefinitionService.findAllByLatestVersions();// 根据最新版本进行查询
		ActionContext.getContext().put("processDefinitionList", processDefinitionList);
		return "list";
	}

	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	public String delete() throws Exception {
		/**
		 * 服务器端通过request.getparamter()时，就进行了一次解码 自己再进行一次URL解码，
		 * 解决get的方式提交导致的中文乱码问题 默认是post方式提交
		 */
		key = URLDecoder.decode(key, "utf-8");
		processDefinitionService.deleteByKey(key);
		return "toList";
	}

	/**
	 * 部署页面
	 */
	public String addUI() {

		return "addUI";
	}

	/**
	 * 部署
	 */
	public String add() throws Exception {

		// 准备部署的文件
		ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(upload));
		try {
			// 部署
			processDefinitionService.deploy(zipInputStream);
		} finally {
			zipInputStream.close();
		}

		return "toList";
	}

	/**
	 * 查看流程图
	 * 
	 * @throws Exception
	 */
	public String downloadProcessImage() throws Exception {

		// 根据id把流程设计器图转化为流，struts2利用<param
		// name="inputName">inputStream</param>实现下载
		id = URLDecoder.decode(id, "utf-8"); // 自己再进行一次URL解码
		inputStream = processDefinitionService.getProcessImageResourceAsStream(id);
		return "downloadProcessImage";
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
