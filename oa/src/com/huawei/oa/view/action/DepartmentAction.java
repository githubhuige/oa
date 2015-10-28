package com.huawei.oa.view.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.huawei.oa.base.ModelDrivenBaseAction;
import com.huawei.oa.domain.Department;
import com.huawei.oa.service.DepartmentService;
import com.huawei.oa.utils.DepartmentUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 *@Controller //意义是声明一个bean，类似于applicationContext.xml中的<bean id=""/>
 * @author Administrator
 *
	//public class DepartmentAction extends ActionSupport implements ModelDriven<Department>{
	这样做依赖于dao层，下面那种做法不依赖于dao 层

 */
@Scope("prototype")
@Controller
public class DepartmentAction extends ModelDrivenBaseAction<Department> {

	//通过BaseAction来获取封装的Department
	/*@Resource
	private DepartmentService departmentService;*/
	
	/*Department model = new Department();
	@Override
	public Department getModel() {
		// TODO Auto-generated method stub
		return model;
	}*/
	//parentId没有，通过页面传过来，接受传过来的数据
	private Long parentId;
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	} 
	/**
	 * 列表
	 */
	public String list(){
		//查找
		List<Department> listDepartment = null;
		//没上一级部门
		if(parentId == null){
			//查找父部门
			 listDepartment = departmentService.fildTopList();
		}else{
			//查找子部门
			listDepartment = departmentService.fildChildrenList(parentId);
			
			//parent在model中，但是没有值，所以需要通过departmentService.getById()
			//获取parent的对象，然后存在值栈的map集合中，通过#parent.parent.id取出来
			Department parent = departmentService.getById(parentId); //查到上一级部门
			ActionContext.getContext().put("parent", parent); //放在map集合中
			
		}
		//存到值栈的map集合中
		ActionContext.getContext().put("listDepartment", listDepartment);
		return "list";
	}
	/**
	 * 添加
	 */
	public String add(){
		//通过设置属性添加，也可以使用model[封装了属性信息]
		//又Modl中没有parentId,所以需要在model中存
		Department department = departmentService.getById(parentId);
		model.setParent(department);
		//保存到数据库中
		departmentService.save(model);
		return "toList";
	}
	/**
	 * 删除
	 */
	public String delete(){
		
		departmentService.delete(model.getId());
		return "toList";
	}
	/**
	 * 修改
	 */
	public String update(){
		//获取源对象
		Department department = departmentService.getById(model.getId());
		//更新属性
		department.setName(model.getName());
		department.setDescription(model.getDescription());
		department.setParent(departmentService.getById(parentId));//设置所属的上级部门
		//更新到数据库中
		departmentService.update(department);
		return "toList";
	}
	/**
	 * 添加页面
	 */
	public String addUI(){
		//准备数据
		List<Department> topList = departmentService.fildTopList();
		List<Department> listDepartment =  DepartmentUtils.getAllDepartment(topList);
		ActionContext.getContext().put("listDepartment", listDepartment);
		return "addUI";
	}
	/**
	 * 修改页面
	 */
	public String updateUI(){
		//准备数据listDepartment,存在map集合中，在页面显示所有的部门，供修改时选择哪个部门
		List<Department> topList = departmentService.fildTopList();
		List<Department> listDepartment =  DepartmentUtils.getAllDepartment(topList);
	
		ActionContext.getContext().put("listDepartment", listDepartment);
		
		//准备回显数据，这里显示的某个部门信息
		Department department = departmentService.getById(model.getId());
		ActionContext.getContext().getValueStack().push(department);//回显
		
		//上面的parentId不存在model中，所以从get中获取，先判断数据库中的parent是否为空
		if(department.getParent() != null){
			//不为空。则把parent的id传给parentId，到页面显示，显示的是上级部门
			parentId = department.getParent().getId();
		}
		return "addUI";
	}

}
