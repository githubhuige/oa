package com.huawei.oa.test;

import org.hibernate.SessionFactory;
import org.jbpm.api.ProcessEngine;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest {
	private static ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
	@Test
	public void testSessionFactory(){
		//测试sessionFactory
		SessionFactory sessionFactory = (SessionFactory) ac.getBean("sessionFactory");
		System.out.println(sessionFactory);
	}
	
	@Test
	public void testService(){
		ServiceTest serviceTest = (ServiceTest)ac.getBean("serviceTest");
		serviceTest.saveTwoUser();
	}
	@Test
	public void testProcessEngine(){

		ProcessEngine processEngine = (ProcessEngine) ac.getBean("processEngine");
		System.out.println("-------------->" + processEngine);
	}
}
