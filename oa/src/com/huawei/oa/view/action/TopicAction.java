package com.huawei.oa.view.action;

import java.util.Date;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.huawei.oa.base.ModelDrivenBaseAction;
import com.huawei.oa.domain.Forum;
import com.huawei.oa.domain.Reply;
import com.huawei.oa.domain.Topic;
import com.huawei.oa.utils.HqlHelper;
import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings("unchecked")
@Controller
@Scope("prototype")
public class TopicAction extends ModelDrivenBaseAction<Topic> {

	// 从页面获取是哪个板块的,model中没有就获取
	private Long forumId;

	// 从页面获取传递过来的 是哪一页,默认是1
	// 因为只要是分页就用到这个属性，所以把它放到baseAtion中，公用
	// private int pageNum = 1;

	// 显示单个主题【包括主贴与回贴列表】
	public String show() {
		// 准备数据topic
		Topic topic = topicService.getById(model.getId());
		ActionContext.getContext().put("topic", topic);
		// 准备数据，为分页的时候
		// List<Reply>replyList = replyService.findByTopic(topic);
		// ActionContext.getContext().put("replyList", replyList);

		// 准备数据：分页用,恢复列表的分页信息
		// pageBean封装了分页的一切信息：pageNum, pageSize, recordList,
		// recordCount，beginPageIndex,endPageIndex,pageCount
		// PageBean pageBean = replyService.getPageBean(pageNum,topic);
		// //把它放在栈顶
		// ActionContext.getContext().getValueStack().push(pageBean);

		// 公共的方法获取分页的信息
		new HqlHelper(Reply.class, "r")//
				.addCondition("r.topic=?", topic)//
				.addOrder("r.postTime", true)//
				.buildPageBeanForStruts2(pageNum, replyService);
		return "show";
	}

	// 发表新主题页面【如果add与update共享一个页面，可以返回saveUI，之前都是用addUI,这样不好】
	public String addUI() {
		// 准备数据forum
		Forum forum = forumService.getById(forumId);
		ActionContext.getContext().put("forum", forum);
		return "addUI";
	}

	// 发表新主题
	public String add() {
		// 封装
		// >> 表单中的字段, 已经封装了 title, content, faceIcon
		model.setForum(forumService.getById(forumId));
		// >> 当前可以直接获取的信息
		model.setIpAddr(ServletActionContext.getRequest().getRemoteAddr());// 发表主题的地址
		model.setPostTime(new Date());// 发表时间
		model.setAuthor(getCurrentUser());// 作者，当前登录的用户
		// >> 应放到业务方法中的一个其他设置
		// topic.setLastReply(lastReply);
		// topic.setLastUpdateTime(lastUpdateTime);
		// topic.setReplyCount(replyCount);
		// topic.setType(type);
		topicService.save(model);
		return "toShow";
	}

	public Long getForumId() {
		return forumId;
	}

	public void setForumId(Long forumId) {
		this.forumId = forumId;
	}
	// public int getPageNum() {
	// return pageNum;
	// }
	// public void setPageNum(int pageNum) {
	// this.pageNum = pageNum;
	// }

}
