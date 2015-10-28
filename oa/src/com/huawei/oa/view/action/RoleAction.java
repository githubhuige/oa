package com.huawei.oa.view.action;

import java.util.HashSet;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.huawei.oa.base.ModelDrivenBaseAction;
import com.huawei.oa.domain.Privilege;
import com.huawei.oa.domain.Role;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
//public class RoleAction extends ActionSupport implements ModelDriven<Role> {
public class RoleAction extends ModelDrivenBaseAction<Role>{

	/**
	 * //第一种方案：通过set和get属性，但是由于在实体中已经设置了这些属性，再设置就有点多余了
	 * 通过属性的set和get方法就可以保存和获取页面传进来的数据
	 */
	// private Long id;
	// private String name;
	// private String description;

	//第二种方案，通过ModelDriver封装页面信息
	/*
	Role model = new Role();

	 *
	 * ModelDriven<Role>:原理是：
	 * //action的类型是modeldriver【因为实现了modeldriver，所以类型是modeldriver】 
	 * if (action instanceof ModelDriven) {
	 *  	ModelDriven modelDriven = (ModelDriven) action;
	 * 		ValueStack stack = invocation.getStack(); 
	 * 		Object model = modelDriven.getModel(); //调用getmodel()获取model 
	 * 		if (model != null) {
	 * 			stack.push(model); //把model压入栈顶【即第一个，先进后出的结构】
	 * 		 }
	 * 
	 * @return
	 
	@Override
	public Role getModel() {
		// TODO Auto-generated method stub
		return model;
	}
*/
	/*@Resource
	private RoleService roleService;*/
	
	private Long[] privilegeIds;

	// 列表
	public String list() {
		List<Role> listRole = roleService.findAll();
		// 与API解耦。如果用servletActionContext.getRequest，则与API偶合了，不利于测试
		ActionContext.getContext().put("listRole", listRole);
		return "list";
	}

	// 删除
	public String delete() {
		// 传进来的数据都保存在model中了
		roleService.delete(model.getId());
		return "toList";
	}

	// 添加
	public String add() {

		//Role role = new Role();
		// role.setName(name);
		// role.setDescription(description);
		//roleService.add(role);
		
		//model封装了属性信息
		roleService.save(model);
		return "toList";
	}

	// 修改
	public String update() {
		// 从数据库中取出原对象
		Role role = roleService.getById(model.getId());
		// 更新属性
		role.setName(model.getName());
		role.setDescription(model.getDescription());
		// 更新到数据库中
		roleService.update(role);
		return "toList";
	}

	// 添加页面
	public String addUI() {

		return "addUI";
	}

	// 修改页面
	public String updateUI() {
		Role role = roleService.getById(model.getId());
		// this.name是updateUI中的属性name
		//this.name = role.getName();
		//this.description = role.getDescription();
		ActionContext.getContext().getValueStack().push(role);//把它放在栈顶
		return "updateUI";
	}

	//设置权限
	public String setPrivilege(){
		//从数据库中取出源对象
		Role role = roleService.getById(model.getId());
		//设置要修改的属性
		List<Privilege>listPrivilege = privilegeService.getByIds(privilegeIds);
		role.setPrivileges(new HashSet<Privilege>(listPrivilege));
		
		//保存
		roleService.update(role);
		return "toList";
	}
	//设置权限页面
	public String setPrivilegeUI(){
		//准备数据
		Role role = roleService.getById(model.getId());
		ActionContext.getContext().put("role", role);
		//为了显示树状图
		List<Privilege> listTopPrivilege = privilegeService.listTopPrivilege();
		ActionContext.getContext().put("listTopPrivilege", listTopPrivilege);
		if(role.getPrivileges().size() > 0){
			privilegeIds = new Long[role.getPrivileges().size()];
			int index = 0;
			for(Privilege privilege : role.getPrivileges()){
				//回显数据
				privilegeIds[index++] = privilege.getId();
			}
		}
		return "setPrivilegeUI";
	}

	public Long[] getPrivilegeIds() {
		return privilegeIds;
	}

	public void setPrivilegeIds(Long[] privilegeIds) {
		this.privilegeIds = privilegeIds;
	}


	
	/*
	 * public Long getId() { return id; }
	 * 
	 * public void setId(Long id) { this.id = id; }
	 * 
	 * public String getName() { return name; }
	 * 
	 * public void setName(String name) { this.name = name; }
	 * 
	 * public void setDescription(String description) { this.description =
	 * description; }
	 * 
	 * public String getDescription() { return description; }
	 */
}