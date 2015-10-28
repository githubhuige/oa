package com.huawei.oa.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.huawei.oa.base.BaseDaoImpl;
import com.huawei.oa.cfg.Configuration;
import com.huawei.oa.domain.Forum;
import com.huawei.oa.domain.PageBean;
import com.huawei.oa.domain.Reply;
import com.huawei.oa.domain.Topic;
import com.huawei.oa.service.ReplyService;

@Service
@SuppressWarnings("unchecked")
public class ReplyServiceImpl extends BaseDaoImpl<Reply> implements
		ReplyService {

	@Override
	@Deprecated//使用分页代替这个方法
	public List<Reply> findByTopic(Topic topic) {
		// 排序：最前面的是最早发表的回帖
		return getSession().createQuery(//
				"FROM Reply r WHERE r.topic=? ORDER BY r.postTime ASC")//
				.setParameter(0, topic)//
				.list();
	}

	@Override
	public void save(Reply reply) {
		// 保存
		getSession().save(reply);
		// 维护相关的信息
		Topic topic = reply.getTopic();
		Forum forum = topic.getForum();
		// 版块的文章数（主题+回复）
		forum.setArticleCount(forum.getArticleCount() + 1);
		// 主题的回复数
		topic.setReplyCount(topic.getReplyCount() + 1);
		// 主题的最后发表的回复
		topic.setLastReply(reply);
		// 主题的最后更新的时间（主题的发表时间或是最后回复的时间）
		topic.setLastUpdateTime(topic.getPostTime());
		getSession().update(forum);
		getSession().update(topic);

	}

	@Deprecated//过时了，使用公用的分页方法
	@Override
	public PageBean getPageBean(int pageNum, Topic topic) {
		int pageSize = Configuration.getPageSize();
		//获取页面的数据列表
		List recordList = getSession().createQuery(//
				"FROM Reply r WHERE r.topic=? ORDER BY r.postTime ASC")//
				.setParameter(0, topic)//
				.setFirstResult((pageNum-1) * pageSize)//每页从哪里开始
				.setMaxResults(pageSize)//每页显示多少条记录
				.list(); 
		
		//获取总记录数
		Long recordCount =  (Long)getSession().createQuery(//
				"SELECT COUNT(*) FROM Reply r WHERE r.topic=?")//
				.setParameter(0, topic)//
				.uniqueResult();
		return new PageBean(pageNum, pageSize, recordList, recordCount.intValue());
	}

}
