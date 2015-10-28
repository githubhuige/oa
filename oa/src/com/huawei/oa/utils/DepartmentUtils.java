package com.huawei.oa.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.huawei.oa.domain.Department;

public class DepartmentUtils {

	/**
	 * 遍历所有的部门树，得到所有的部门表，并修改名称，以表示层次
	 * @param topList
	 * @return
	 */
	public static List<Department> getAllDepartment(List<Department> topList) {
		//把修改名后的部门存到集合list中
		List<Department> list = new ArrayList<Department>();
		walkDepartmentTree(topList,"▏▬",list);
		return list;
	}
	//遍历所有的部门树，把遍历出来的部门放在指定的集合中
	public static void walkDepartmentTree(Collection<Department> topList,String prefix,List<Department> list){
		for(Department top : topList){
			//顶点
			//并没有修改数据库中的数据
			Department copy = new Department();//原对象是session中的对象，是持久化状态，所以要使用副本【这个不懂】
			copy.setId(top.getId());
			copy.setName(prefix + top.getName());
			list.add(copy);
			//子树
			walkDepartmentTree(top.getChildren(),"　" + prefix, list);
		}
	}

}
