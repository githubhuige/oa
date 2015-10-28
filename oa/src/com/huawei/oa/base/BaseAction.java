package com.huawei.oa.base;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;

import com.huawei.oa.domain.User;
import com.huawei.oa.service.ApplicationService;
import com.huawei.oa.service.ApplicationTemplateService;
import com.huawei.oa.service.DepartmentService;
import com.huawei.oa.service.ForumService;
import com.huawei.oa.service.PrivilegeService;
import com.huawei.oa.service.ProcessDefinitionService;
import com.huawei.oa.service.ReplyService;
import com.huawei.oa.service.RoleService;
import com.huawei.oa.service.TopicService;
import com.huawei.oa.service.UserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport {
	@Resource
	protected DepartmentService departmentService;
	@Resource
	protected UserService userService;
	@Resource
	protected RoleService roleService;
	@Resource
	protected PrivilegeService privilegeService;
	@Resource
	protected ForumService forumService;
	@Resource
	protected ReplyService replyService;
	@Resource
	protected TopicService topicService;
	@Resource
	protected ProcessDefinitionService processDefinitionService;
	@Resource
	protected ApplicationTemplateService applicationTemplateService;
	@Resource
	protected ApplicationService applicationService;

	public User getCurrentUser() {

		User user = (User) ActionContext.getContext().getSession().get("user");
		return user;
	}

	//当前页
	protected int pageNum = 1;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	protected String uploadFile(File upload) {
		// 封装
		// ---获取存储在服务器端的真实路径
		String basePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF/upload_files");

		// ----当一个文件夹中存放的文件很多时，就会导致打开很慢，所以文件夹的建立一般以时间为名
		SimpleDateFormat df = new SimpleDateFormat("/yyyy/MM/dd/");
		String subPath = df.format(new Date());

		// ---保存在数据库中的文件名，为了防止重复，使用uuid保存
		String path = basePath + subPath + UUID.randomUUID().toString();

		// 把上传到临时文件夹中的文件移到服务器端的文件路径下
		// ----当文件夹不存在或者文件已经存在时，会报错
		File dir = new File(basePath + subPath);
		if (!dir.exists()) {
			dir.mkdirs();// 创建文件夹
		}

		upload.renameTo(new File(path));
		return path;
	}
}
