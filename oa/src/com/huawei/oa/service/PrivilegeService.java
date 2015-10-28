package com.huawei.oa.service;

import java.util.List;

import com.huawei.oa.base.BaseDao;
import com.huawei.oa.domain.Privilege;

public interface PrivilegeService  extends BaseDao<Privilege> {

	List<Privilege> listTopPrivilege();

	List<Privilege> getAllPrivilegeUrls();

}
