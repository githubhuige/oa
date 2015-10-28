package com.huawei.oa.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.huawei.oa.base.BaseDaoImpl;
import com.huawei.oa.domain.Privilege;
import com.huawei.oa.service.PrivilegeService;
@Service
@SuppressWarnings("unchecked")
public class PrivilegeServiceImpl extends BaseDaoImpl<Privilege> implements PrivilegeService{

	@Override
	public List<Privilege> listTopPrivilege() {
		return getSession().createQuery(//
				"FROM Privilege p WHERE p.parent IS NULL")//
				.list();
	}

	//查询所有的url，且不能重复和空值
	@Override
	public List<Privilege> getAllPrivilegeUrls() {
		return getSession().createQuery(//
				"SELECT DISTINCT p.url FROM Privilege p WHERE p.url IS NOT NULL")//
				.list();
	}

}
