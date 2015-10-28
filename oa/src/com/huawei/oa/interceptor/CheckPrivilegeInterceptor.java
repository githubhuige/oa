package com.huawei.oa.interceptor;


import com.huawei.oa.domain.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
/**
 * AbstractInterceptor:抽象类；实现了init与destrory
 * @author Administrator
 * 这个拦截器是拦截每一次请求，之前做的都是判断用户的权限，
 * 	而这里的方法作用是：当用户不经过登陆，或者没有这个权限，直接可以通过在地址栏上输入地址，那么照样可以访问
 * 	所以，这样不好
 *
 */
@SuppressWarnings("unchecked")
public class CheckPrivilegeInterceptor extends AbstractInterceptor{

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		
		//从session中获取user对象
		User user = (User) ActionContext.getContext().getSession().get("user");
		
		// 获取当前访问的URL，并去掉当前应用程序的前缀（也就是 当前的url为namespaceName + actionName ）
		String namespaceName = invocation.getProxy().getNamespace(); //得到命名空间
		String actionName = invocation.getProxy().getActionName();   //得到action名字
		String privilegeUrl = namespaceName + actionName;
		//因为命名空间可能为/aa,而默认命名空间为/  ：/aa/userAction_login.action 默认：/userAction_login
		if(namespaceName.endsWith("/")){
			privilegeUrl = namespaceName + actionName;
		}else{
			privilegeUrl = namespaceName + "/" + actionName;
		}
		//因为privilegeUrl  = /aa/userAction_login.action ，完整的url是aa/userAction_login.action，所以得去掉/ 
		if(privilegeUrl.startsWith("/")){
			privilegeUrl = privilegeUrl.substring(1);//去掉“/”
		}
		//用户未登录
		if(user == null){
			if(privilegeUrl.startsWith("userAction_login")){// userAction_login, userAction_loginUI
			//在登陆的界面，放行
				return invocation.invoke();
			}else{
			//不是在登陆的界面，转到登陆界面
				return "loginUI";
			}
		}else{
		//用户已登录（判断权限）
			if(user.hasPrivilegeByUrl(privilegeUrl)){
			//用户有权限，就放行
				return invocation.invoke();
			}else{
			//如果没有权限，转到提示页面
				return "noPrivilegeError";
			}
		}
	}

}
