package com.huawei.oa.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huawei.oa.base.BaseAction;
import com.huawei.oa.base.BaseDaoImpl;
import com.huawei.oa.dao.RoleDao;
import com.huawei.oa.domain.Role;
import com.huawei.oa.service.RoleService;

@Service
//@Transactional//在父类BaseDaoImpl中已经有这个注解了
// service层管理事物
public class RoleServiceImpl extends BaseDaoImpl<Role> implements RoleService {

	/*@Resource
	private RoleDao roleDao;

	@Override
	public List<Role> findAll() {

		return roleDao.findAll();
	}

	@Override
	public void delete(Long id) {

		roleDao.delete(id);
	}

	@Override
	public Role getById(Long id) {

		return roleDao.getById(id);
	}

	@Override
	public void update(Role role) {

		roleDao.update(role);
	}

	@Override
	public void save(Role model) {

		roleDao.save(model);
	}*/

}
