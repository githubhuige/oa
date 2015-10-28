package com.huawei.oa.service;

import java.util.List;

import com.huawei.oa.base.BaseDao;
import com.huawei.oa.domain.Department;

public interface DepartmentService extends BaseDao<Department>{

	List<Department> fildTopList();

	List<Department> fildChildrenList(Long parentId);

//	List<Department> findAll();
//
//	void save(Department model);
//
//	Department getById(Long id);
//
//	void delete(Long id);
//
//	void update(Department department);

}
