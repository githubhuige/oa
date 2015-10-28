package com.huawei.oa.test;

import org.junit.Test;

import com.huawei.oa.dao.RoleDao;
import com.huawei.oa.dao.UserDao;
import com.huawei.oa.dao.impl.RoleDaoImpl;
import com.huawei.oa.dao.impl.UserDaoImpl;

public class BaseDaoTest {

	@Test
	public void testGetById() {
		UserDao userDao = new UserDaoImpl();
		RoleDao roleDao = new RoleDaoImpl();
		userDao.delete(1L);
		roleDao.delete(1L);
	}

}
