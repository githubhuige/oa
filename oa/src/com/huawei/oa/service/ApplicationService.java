package com.huawei.oa.service;

import java.util.List;

import com.huawei.oa.base.BaseDao;
import com.huawei.oa.domain.Application;
import com.huawei.oa.domain.ApproveInfo;
import com.huawei.oa.domain.TaskView;
import com.huawei.oa.domain.User;

public interface ApplicationService extends BaseDao<Application>{

	void submit(Application application);

	List<TaskView> getMyTaskViewList(User currentUser);

	void approve(ApproveInfo approveInfo, String taskId);

}
