package com.huawei.oa.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;
import org.springframework.stereotype.Service;

import com.huawei.oa.base.BaseDaoImpl;
import com.huawei.oa.domain.Application;
import com.huawei.oa.domain.ApproveInfo;
import com.huawei.oa.domain.TaskView;
import com.huawei.oa.domain.User;
import com.huawei.oa.service.ApplicationService;

@Service
public class applicationServiceImpl extends BaseDaoImpl<Application> implements ApplicationService {

	@Resource
	private ProcessEngine processEngine;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public void submit(Application application) {
		// 1，设置属性并保存application（不在submitUI中的数据）
		application.setApplyTime(new Date());// 申请时间
		application.setStatus(application.STATUS_RUNNING);// 流程状态（审批中）
		application.setTitle(application.getApplicationTemplate().getName()//
				+ "_" + application.getApplicant().getName()//
				+ "_" + sdf.format(application.getApplyTime()));// 格式为：{模板名称}_{申请人姓名}_{申请时间}
		// 保存申请信息
		getSession().save(application);

		// 并启动流程开始流转
		// >> 准备流程变量
		Map<String, Object> variablesMap = new HashMap<String, Object>();
		variablesMap.put("application", application);

		// >> 启动程程实例，并带上流程变量（当前的申 请信息）
		String pdKey = application.getApplicationTemplate().getProcessDefinitonKey();
		ProcessInstance pid = processEngine.getExecutionService().startProcessInstanceById(pdKey, variablesMap);
		// >> 办理完第1个任务“提交申请”
		Task task = processEngine.getTaskService()//
				.createTaskQuery()//
				.processDefinitionId(pid.getId())//
				.uniqueResult();// 查询出本流程实例中当前仅有的一个任务“提交申请”
		processEngine.getTaskService().completeTask(task.getId());

	}

	@Override
	public List<TaskView> getMyTaskViewList(User currentUser) {
		// 查询我的任务列表
		String userId = currentUser.getLoginName();// 约定使用loginName作为JBPM用的用户标识符
		List<Task> taskList = processEngine.getTaskService().findPersonalTasks(userId);

		// 找出每个任务对应的申请信息
		List<TaskView> resultList = new ArrayList<TaskView>();
		for (Task task : taskList) {
			Application application = (Application) processEngine.getTaskService().getVariable(task.getId(), "application");
			resultList.add(new TaskView(task, application));
		}
		// 返回“任务--申请信息”的结果
		return resultList;
	}

	@Override
	public void approve(ApproveInfo approveInfo, String taskId) {

		// 1，保存本次审批信息
		getSession().save(approveInfo);
		// 2，办理完任务
		Task task = processEngine.getTaskService().getTask(taskId);// 一定要先取出Task对象，再完成任务，否则拿不到，因为执行完就变成历史信息了。
		processEngine.getTaskService().getTaskComments(taskId);

		// >> 取出所属的流程实例，如果取出的为null，说明流程实例执行完成了，已经变成了历史记录
		ProcessInstance pi = processEngine.getExecutionService().findProcessInstanceById(task.getExecutionId());

		// 3，维护申请的状态
		Application application = approveInfo.getApplication();
		if (!approveInfo.isApproval()) {
			// 如果本环节不同意，则流程实例直接结束，申请的状态为：未通过
			if (pi != null) {// 如果流程还未结束
				processEngine.getExecutionService().endProcessInstance(task.getExecutionId(), Application.STATUS_REJECTED);
			}
			application.setStatus(Application.STATUS_REJECTED);

		} else {
			// 如果本环节同意，而且本环节是最后一个环节，则流程实例正常结束，申请的状态为：已通过
			if (pi == null) {// 本环节是最后一个环节，即流程已经结束了
				application.setStatus(Application.STATUS_APPROVED);
			}
			application.setStatus(Application.STATUS_APPROVED);
		}
		getSession().update(application);
	}
}
