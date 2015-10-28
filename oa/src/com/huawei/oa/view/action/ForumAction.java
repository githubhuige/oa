package com.huawei.oa.view.action;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.huawei.oa.base.ModelDrivenBaseAction;
import com.huawei.oa.domain.Forum;
import com.huawei.oa.domain.PageBean;
import com.huawei.oa.domain.Topic;
import com.huawei.oa.utils.HqlHelper;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
@SuppressWarnings("unchecked")
public class ForumAction extends ModelDrivenBaseAction<Forum> {

	/**
	 * 0 表示全部主题 <br>
	 * 1 表示只看精华帖
	 */
	private int viewType = 0;

	/**
	 * 0 代表默认排序(所有置顶帖在前面，并按最后更新时间降序排列)<br>
	 * 1 代表只按最后更新时间排序<br>
	 * 2 代表只按主题发表时间排序<br>
	 * 3 代表只按回复数量排序
	 */
	private int orderBy = 0;

	/**
	 * true 表示升序<br>
	 * false 表示降序
	 */
	private boolean asc = false;

	// 版块列表
	public String list() {
		List<Forum> forumList = forumService.findAll();
		ActionContext.getContext().put("forumList", forumList);
		return "list";
	}

	// 显示单个版块【单个板块中所有的主题】
	public String show() {
		// 准备数据forum【需要显示的是哪个板块的主题】
		Forum forum = forumService.getById(model.getId());
		ActionContext.getContext().put("forum", forum);
		// 准备数据topicList【当前板块的所有主题】
		// List<Topic> topicList = topicService.findByForum(forum);
		// ActionContext.getContext().put("topicList", topicList);

		// 准备数据【当前板块的所有主题的分页信息】
		// PageBean pageBean = topicService.getPageBean(pageNum, forum);
		// ActionContext.getContext().getValueStack().push(pageBean);

		// 使用公用方法分页
		new HqlHelper(Topic.class, "t")//
				.addCondition("t.forum=?", forum)//
				.addCondition(viewType == 1, "t.type=?", Topic.TYPE_BEST)// 只看精华帖
				.addOrder(orderBy == 1, "t.lastUpdateTime", asc)// 1 代表只按最后更新时间排序,asc由页面传进来
				.addOrder(orderBy == 2, "t.postTime", asc)// 2代表只按主题发表时间排序<br>
				.addOrder(orderBy == 3, "t.replyCount", asc)// 3 代表只按回复数量排序
				.addOrder(orderBy == 0, "(CASE t.type WHEN 2 THEN 2 ELSE 0 END)", false)// 0代表默认排序(所有置顶帖在前面，并按最后更新时间降序排列)<br>
				.addOrder(orderBy == 0, "t.lastUpdateTime", false)// 0代表默认排序(所有置顶帖在前面，并按最后更新时间降序排列)<br>
				.buildPageBeanForStruts2(pageNum, topicService);// 查询
		return "show";
	}

	public int getViewType() {
		return viewType;
	}

	public void setViewType(int viewType) {
		this.viewType = viewType;
	}

	public int getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isAsc() {
		return asc;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}

}
