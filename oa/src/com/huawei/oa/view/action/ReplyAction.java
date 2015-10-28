package com.huawei.oa.view.action;

import java.util.Date;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.huawei.oa.base.ModelDrivenBaseAction;
import com.huawei.oa.domain.Reply;
import com.huawei.oa.domain.Topic;
import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings("unchecked")
@Controller
@Scope("prototype")
public class ReplyAction extends ModelDrivenBaseAction<Reply> {

	private Long topicId;

	// 发表新回复页面
	public String addUI() {
		// 准备数据topic
		Topic topic = topicService.getById(topicId);
		ActionContext.getContext().put("topic", topic);
		return "addUI";
	}

	// 发表新回复
	public String add() {
		// 封装content,faceicon,title
		model.setTopic(topicService.getById(topicId));
		
		model.setAuthor(getCurrentUser());
		model.setIpAddr(ServletActionContext.getRequest().getRemoteAddr());
		model.setPostTime(new Date());
		//其他业务信息
		// 保存
		replyService.save(model);
		return "toTopicShow";// 转到新回复所在的新主题页面
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

}
