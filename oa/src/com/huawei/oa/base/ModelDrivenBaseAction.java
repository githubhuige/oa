package com.huawei.oa.base;

import java.lang.reflect.ParameterizedType;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import com.huawei.oa.dao.DepartmentDao;
import com.huawei.oa.domain.Privilege;
import com.huawei.oa.domain.User;
import com.huawei.oa.service.DepartmentService;
import com.huawei.oa.service.ForumService;
import com.huawei.oa.service.PrivilegeService;
import com.huawei.oa.service.ReplyService;
import com.huawei.oa.service.RoleService;
import com.huawei.oa.service.TopicService;
import com.huawei.oa.service.UserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @Resource
	private DepartmentService departmentService;
	@Resource
	protected UserService userService;
	@Resource
	protected RoleService roleService;
	// 子包中也可以访问
	protected T model;
	
	上面这些东西都是一样的，所以直接封装在这样类中
 * 
 * @author Administrator
 *
 * @param <T>
 */

@SuppressWarnings("unchecked")
public abstract class ModelDrivenBaseAction<T> extends BaseAction implements ModelDriven<T> {

	
	// 子包中也可以访问
	protected T model;

	public ModelDrivenBaseAction() {
		// 获取传进来的对象类型
		ParameterizedType pt = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		// 根据类型创建对象
		Class clazz = (Class) pt.getActualTypeArguments()[0];
		try {
			model = (T) clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public T getModel() {
		return model;
	}
	
}
