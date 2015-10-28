package com.huawei.oa.view.listener;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.huawei.oa.domain.Privilege;
import com.huawei.oa.service.PrivilegeService;
@SuppressWarnings("unchecked")
public class InitServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		/**
		 * 分析： 目的：是为了把顶级菜单放到application中
		 * 如何把顶级菜单放在application中：通过application.setAbri(); 但是如何得到顶级菜单呢：
		 * 1通过privilegeService.findTopList()
		 * 但是如何得到privilegeService对象，因为privilegeService由spring容器管理，所以通过spring容器获取
		 * 问题来了，InitServletContextListener不在容器中，如何获取呢？
		 * 方法：1WebApplicationContextUtils
		 * .getWebApplicationContext(application);得到 得到容器service实例对象
		 * 2这样就可以跟在容器中做法一样了
		 */
		// new 一个application
		ServletContext application = arg0.getServletContext();
		// 得到service实例对象
		ApplicationContext ac = WebApplicationContextUtils
				.getWebApplicationContext(application);
		PrivilegeService privilegeService = (PrivilegeService) ac
				.getBean("privilegeServiceImpl");

		// 准备所有顶级权限集合（顶级菜单）
		List<Privilege> topPrivilegeList = privilegeService.listTopPrivilege();
		application.setAttribute("topPrivilegeList", topPrivilegeList);
		// 准备所有权限集合
		List<Privilege>allPrivilegeUrls = privilegeService.getAllPrivilegeUrls();
		application.setAttribute("allPrivilegeUrls", allPrivilegeUrls);

	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

}
