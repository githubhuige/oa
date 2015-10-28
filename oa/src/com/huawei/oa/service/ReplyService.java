package com.huawei.oa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.huawei.oa.base.BaseDao;
import com.huawei.oa.domain.PageBean;
import com.huawei.oa.domain.Reply;
import com.huawei.oa.domain.Topic;

@Service
public interface ReplyService extends BaseDao<Reply> {

	List<Reply> findByTopic(Topic topic);

	PageBean getPageBean(int pageNum, Topic topic);

}
