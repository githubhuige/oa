package com.huawei.oa.view.action;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.huawei.oa.base.ModelDrivenBaseAction;
import com.huawei.oa.domain.Department;
import com.huawei.oa.domain.Role;
import com.huawei.oa.domain.User;
import com.huawei.oa.utils.DepartmentUtils;
import com.huawei.oa.utils.HqlHelper;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
public class UserAction extends ModelDrivenBaseAction<User> {

	/**
	 * 通过set和get方法获取页面传递进来的id【先从modelDriver中查找有没有封装了departmentId, 没有则从private
	 * Long departmentId;这里找】
	 */
	private Long departmentId;
	private Long[] roleIds; // 多个岗位被选中

	public Long[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Long[] roleIds) {
		this.roleIds = roleIds;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public String list() {
		// List<User> listUser = userService.findAll();
		//
		// ActionContext.getContext().put("listUser", listUser);

		// 公共方法获取分页信息

		new HqlHelper(User.class, "u")//
				.buildPageBeanForStruts2(pageNum, userService);
		return "list";
	}

	public String update() {
		// 根据id获取对象
		User user = userService.getById(model.getId());
		// 更新基本属性
		user.setDescription(model.getDescription());
		user.setEmail(model.getEmail());
		user.setGender(model.getGender());
		user.setLoginName(model.getLoginName());
		user.setName(model.getName());
		user.setPhoneNumber(model.getPhoneNumber());
		// 更新department与role
		Department department = departmentService.getById(departmentId);
		user.setDepartment(department);
		List<Role> roleList = roleService.getByIds(roleIds);
		user.setRoles(new HashSet<Role>(roleList));
		// 更新到数据库中
		userService.update(user);
		return "toList";
	}

	public String delete() {
		userService.delete(model.getId());
		return "toList";
	}

	public String add() {
		// 创建对象，设置属性（也可以通过model）
		// 1由于model是user，所以department和role没有存到valueStack中
		model.setDepartment(departmentService.getById(departmentId));

		// 设置role的值
		List<Role> listRole = roleService.getByIds(roleIds);
		// Set<Role> roles = new HashSet<Role>();
		// roles.addAll(listRole); //addAll（）方法把一个集合存放到set集合中
		// model.setRoles(roles);
		model.setRoles(new HashSet<Role>(listRole)); // 与上面等价
		model.setPassword(DigestUtils.md5Hex("1234"));// 默认密码是1234，应该使用Md5摘要
		// 保存到数据库中
		userService.save(model);
		return "toList";
	}

	public String addUI() {
		// 准备数据：listDepartment
		// TODO 应是显示树状结构，先使用所有的部门列表代替
		List<Department> listDepartment = null;
		if (departmentId == null) {
			List<Department> topList = departmentService.fildTopList();
			listDepartment = DepartmentUtils.getAllDepartment(topList);
		} else {
			listDepartment = departmentService.fildChildrenList(departmentId);
		}
		ActionContext.getContext().put("listDepartment", listDepartment);
		// 准备数据roleIds
		List<Role> listRole = roleService.findAll();
		ActionContext.getContext().put("listRole", listRole);
		return "addUI";
	}

	public String updateUI() {
		// 准备数据departmentList
		// TODO 应是显示树状结构，先使用所有的部门列表代替
		List<Department> listDepartment = null;
		if (departmentId == null) {
			List<Department> topList = departmentService.fildTopList();
			listDepartment = DepartmentUtils.getAllDepartment(topList);
		} else {
			listDepartment = departmentService.fildChildrenList(departmentId);
		}
		ActionContext.getContext().put("listDepartment", listDepartment);
		// 准备数据roleList
		List<Role> listRole = roleService.findAll();
		ActionContext.getContext().put("listRole", listRole);

		// 准备回显数据
		User user = userService.getById(model.getId());
		ActionContext.getContext().getValueStack().push(user);
		// 从数据库中获取的数据赋值给页面属性
		if (user.getDepartment() != null) {
			departmentId = user.getDepartment().getId();
		}

		if (user.getRoles().size() > 0) {
			int index = 0;
			roleIds = new Long[user.getRoles().size()];
			for (Role role : user.getRoles()) {
				roleIds[index++] = role.getId();
			}

		}
		return "addUI";
	}

	/** 初始化密码为“1234” */
	public String initPassword() {
		// 从数据库中取出源对象
		User user = userService.getById(model.getId());
		// 更新属性,使用md5摘要
		String md5Password = DigestUtils.md5Hex("1234");
		user.setPassword(md5Password);
		// 保存到数据库中
		userService.update(user);
		return "toList";
	}

	// 登陆页面
	public String loginUI() {

		return "loginUI";
	}

	// 登陆
	public String login() {
		User user = userService.getLoginNameAndPassword(model.getLoginName(),
				model.getPassword());
		if (user == null) {
			addFieldError("login", "用户名或者密码错误!");
			return "loginUI";
		} else {
 			// 存在session中
			ActionContext.getContext().getSession().put("user", user);
			return "index";
		}
	}

	// 注销
	public String loginOut() {
		// 移除session
		ActionContext.getContext().getSession().remove("user");
		return "loginOut";
	}
}
