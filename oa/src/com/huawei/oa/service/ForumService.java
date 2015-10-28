package com.huawei.oa.service;

import com.huawei.oa.base.BaseDao;
import com.huawei.oa.domain.Forum;

public interface ForumService extends BaseDao<Forum>{

	void moveUp(Long id);

	void moveDown(Long id);

}
