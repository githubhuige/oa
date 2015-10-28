package com.huawei.oa.view.action;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.huawei.oa.base.ModelDrivenBaseAction;
import com.huawei.oa.domain.Forum;
import com.huawei.oa.service.ForumService;
import com.opensymphony.xwork2.ActionContext;

@Controller
@SuppressWarnings("unchecked")
@Scope("prototype")
public class ForumManageAction extends ModelDrivenBaseAction<Forum> {
	
	public String list(){
		List<Forum> forumList = forumService.findAll();
		ActionContext.getContext().put("forumList", forumList);
		return "list";
	}
	//添加 
	public String add(){
		forumService.save(model);
		return "toList";
	}
	
	//添加页面
	public String addUI(){
		
		return "addUI";
	}
	
	//修改
	public String update(){
		//获取原对象
		Forum forum = forumService.getById(model.getId());
		//设置要修改的属性
		forum.setName(model.getName());
		forum.setDescription(model.getDescription());
		//更新
		forumService.update(forum);
		return "toList";
	}
	
	//修改页面
	public String updateUI(){
		//准备要回显的数据
		Forum forum = forumService.getById(model.getId());
		//全部在model中。可以通过值栈来获取
		ActionContext.getContext().getValueStack().push(forum);
		return "addUI";
	}
	
	//删除
	public String delete(){
		forumService.delete(model.getId());
		return "toList";
	}
	
	//上移
	public String moveUp(){
		forumService.moveUp(model.getId());
		return "toList";
	}
	
	//下移
	public String moveDown(){
		forumService.moveDown(model.getId());
		return "toList";
	}
}
