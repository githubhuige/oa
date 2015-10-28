package com.huawei.oa.test;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

//因为在struts2配置文件中不知道要调用哪个action类，所以添加一个注解，
@Controller
@Scope("prototype")//因为action类是多例
public class TestAction extends ActionSupport{
	
	@Resource
	private ServiceTest service;
	
	@Override
	public String execute() {
		service.saveTwoUser();
		System.out.println("excute------");
		
		return "success";
	}
}
