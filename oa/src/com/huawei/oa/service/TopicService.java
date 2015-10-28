package com.huawei.oa.service;

import java.util.List;

import com.huawei.oa.base.BaseDao;
import com.huawei.oa.domain.Forum;
import com.huawei.oa.domain.PageBean;
import com.huawei.oa.domain.Topic;

public interface TopicService extends BaseDao<Topic> {

	List<Topic> findByForum(Forum forum);

	PageBean getPageBean(int pageNum, Forum forum);

}
