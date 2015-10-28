package com.huawei.oa.service;

import com.huawei.oa.base.BaseDao;
import com.huawei.oa.domain.User;

public interface UserService extends BaseDao<User>{

	User getLoginNameAndPassword(String loginName, String password);

}
