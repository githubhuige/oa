package com.huawei.oa.test;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huawei.oa.domain.User;

@Service("serviceTest")
public class ServiceTest {
	//获取sessionFactory，创建session对象
	@Resource
	private SessionFactory sessionFactory;
	@Transactional
	public void saveTwoUser(){
		//通过currentSession获取session
		Session session = sessionFactory.getCurrentSession();
		session.save(new User());
//		int i = 1 / 0;
		session.save(new User());
	}
}
