package com.huawei.oa.view.action;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.huawei.oa.base.BaseAction;
import com.huawei.oa.domain.Application;
import com.huawei.oa.domain.ApplicationTemplate;
import com.huawei.oa.domain.ApproveInfo;
import com.huawei.oa.domain.TaskView;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
public class FlowAction extends BaseAction {

	private File upload;
	private Long applicationTemplateId;
	
	private Long applicationId;
	private String taskId;
	private String comment;
	private boolean approval;


	/** 起草申请（模板列表） */
	public String applicationTemplateList() throws Exception {
		List<ApplicationTemplate> applicationTemplates = applicationTemplateService.findAll();
		ActionContext.getContext().put("applicationTemplates", applicationTemplates);
		return "applicationTemplateList";
	}

	/** 提交申请页面 */
	public String submitUI() throws Exception {
		return "submitUI";
	}

	/** 提交申请 */
	public String submit() throws Exception {
		// 封装申请信息（封装了sumbitUI中数据）
		Application application = new Application();
		application.setApplicant(getCurrentUser());// 申请人，当前登陆用户
		application.setPath(uploadFile(upload));// 保存上传的文件并设置路径
		application.setApplicationTemplate(applicationTemplateService.getById(applicationTemplateId));

		// application.setApplyTime(new Date());// 申请时间
		// application.setStatus(status);
		// application.setTitle(title);
		// 调用业务方法（保存申请信息，并启动流程开始流转）
		applicationService.submit(application);
		return "toMyApplicationList";
	}

	/** 我的申请查询 */
	public String myApplicationList() throws Exception {
		return "myApplicationList";
	}

	// ================================== 审批人有关

	/** 待我审批（我的任务列表） */
	public String myTaskList() throws Exception {
		List<TaskView>taskViewList = applicationService.getMyTaskViewList(getCurrentUser());
		ActionContext.getContext().put("taskViewList",taskViewList );
		return "myTaskList";
	}

	/** 审批处理页面 */
	public String approveUI() throws Exception {
		return "approveUI";
	}

	/** 审批处理 */
	public String approve() throws Exception {
		//封装
		ApproveInfo approveInfo = new ApproveInfo();
		approveInfo.setApplication(applicationService.getById(applicationId));//申请的文档
		approveInfo.setApproval(approval);//审批的内容
		approveInfo.setComment(comment);//审批意见

		approveInfo.setApproveTime(new Date());//审批时间
		approveInfo.setApprover(getCurrentUser());//审批人
		
		// 调用用业务方法（保存本次审批信息，并办理完任务，并维护申请的状态）
		applicationService.approve(approveInfo,taskId);
		return "toMyTaskList"; // 成功后转到待我审批页面
	}

	/** 查看流转记录 */
	public String approveHistory() throws Exception {
		return "approveHistory";
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public Long getApplicationTemplateId() {
		return applicationTemplateId;
	}

	public void setApplicationTemplateId(Long applicationTemplateId) {
		this.applicationTemplateId = applicationTemplateId;
	}

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isApproval() {
		return approval;
	}

	public void setApproval(boolean approval) {
		this.approval = approval;
	}

}
