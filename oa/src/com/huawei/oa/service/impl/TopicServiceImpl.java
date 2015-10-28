package com.huawei.oa.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.huawei.oa.base.BaseDaoImpl;
import com.huawei.oa.cfg.Configuration;
import com.huawei.oa.domain.Forum;
import com.huawei.oa.domain.PageBean;
import com.huawei.oa.domain.Topic;
import com.huawei.oa.service.TopicService;

@Service
@SuppressWarnings("unchecked")
public class TopicServiceImpl extends BaseDaoImpl<Topic> implements TopicService {

	
	public List<Topic> findByForum(Forum forum) {
		//置顶帖放在第一，最新发表的排在置顶帖下面
		/**
		 * ODER BY (CASE t.type WHEN 2 THEN 2 ELSE 0 END) DESC,t.lastUpdateTime DESC
		 * 	CASE t.type WHEN 2 THEN 2 ELSE 0 END:表示类型为2的作为一组，其他的作为一组
		 * 	先按照帖子类型进行排，再按照发表的时间排
		 */
		return getSession().createQuery(//
				"FROM Topic t WHERE t.forum=? ORDER BY (CASE t.type WHEN 2 THEN 2 ELSE 0 END) DESC,t.lastUpdateTime DESC")//
				.setParameter(0, forum)//
				.list();
	}

	@Override
	public void save(Topic topic) {
		// >> 应放到业务方法中的一个其他设置
		// 1，设置属性并保存
//		topic.setLastReply(null);//默认为0，即还没有帖子
//		topic.setReplyCount(0);//回复数量，默认为0，即刚发表没回复
//		topic.setType(0);//帖子类型，默认为普通帖，即为0
		topic.setLastUpdateTime(topic.getPostTime());// 默认为主题的发表时间
		getSession().save(topic);
		// 2，维护相关的信息
		Forum forum = topic.getForum();
		forum.setArticleCount(forum.getArticleCount() + 1);//文章数量；主题数量+回复数量
		forum.setLastTopic(topic);//最后发表的主题
		forum.setTopicCount(forum.getTopicCount() + 1);//主题的数量
		getSession().update(forum);
	}

	@Override
	@Deprecated//过时了，使用公用的分页方法
	public PageBean getPageBean(int pageNum, Forum forum) {
		int pageSize =  Configuration.getPageSize();
		List recordList = getSession().createQuery(//每页的数据列表
				"FROM Topic t WHERE t.forum=? ORDER BY (CASE t.type WHEN 2 THEN 2 ELSE 0 END) DESC,t.lastUpdateTime DESC")//
				.setParameter(0, forum)//
				.setFirstResult((pageNum-1) * pageSize)//
				.setMaxResults(pageSize)//
				.list();
		Long recordCount = (Long)getSession().createQuery(//总记录数
				"SELECT COUNT(*) FROM Topic t WHERE t.forum=? ")//
				.setParameter(0, forum)//
				.uniqueResult();
		return new PageBean(pageNum, pageSize, recordList, recordCount.intValue());
	}

}
