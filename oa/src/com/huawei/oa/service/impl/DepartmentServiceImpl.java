package com.huawei.oa.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huawei.oa.base.BaseDaoImpl;
import com.huawei.oa.dao.DepartmentDao;
import com.huawei.oa.domain.Department;
import com.huawei.oa.service.DepartmentService;

/**
 * @Transactional:这个注解可以不写，因为在BaseAction中已经声明了，所以直接可以继承BaseAction中的注解
 * @author Administrator
 *由于dao层干的事情重复，所以取消掉dao层，让service层直接访问BaseDao层，使代码简单了
 */
@Service
@SuppressWarnings("unchecked")
public class DepartmentServiceImpl extends BaseDaoImpl<Department> implements DepartmentService{

	@Override
	public List<Department> fildTopList() {
		return getSession().createQuery//
				("FROM Department d WHERE d.parent IS NULL")//
				.list();
	}

	@Override
	public List<Department> fildChildrenList(Long parentId) {
		return  getSession().createQuery//
				("FROM Department d WHERE d.parent.id=?")//
				.setParameter(0, parentId)//
				.list();
	}

	/*@Resource
	private DepartmentDao departmentDao;
	@Override
	public List<Department> findAll() {
		return departmentDao.findAll();
	}

	@Override
	public void save(Department department) {

		departmentDao.save(department);
	}

	@Override
	public Department getById(Long id) {
		return departmentDao.getById(id);
	}

	@Override
	public void delete(Long id) {

		departmentDao.delete(id);
	}

	@Override
	public void update(Department department) {

		departmentDao.update(department);
	}*/

}
