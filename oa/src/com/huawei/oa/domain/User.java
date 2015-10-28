package com.huawei.oa.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.Application;

import com.opensymphony.xwork2.ActionContext;

/**
 * 用户
 * @author tyg
 * 
 */
@SuppressWarnings("unchecked")
public class User implements java.io.Serializable{
	private Long id;
	private Department department;
	private Set<Role> roles = new HashSet<Role>();

	private String loginName; // 登录名
	private String password; // 密码
	private String name; // 真实姓名
	private String gender; // 性别
	private String phoneNumber; // 电话号码
	private String email; // 电子邮件
	private String description; // 说明

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	//判断用户是否有权限查看某个菜单
	public boolean hasPrivilegeByName(String privilegeName){
		//超级管理员权限
		if(isAdmin()){
			return true;
		}
		//通过set和get方法得到了user的roles
		for(Role role : roles){
			//角色有哪些权限
			for(Privilege privilege : role.getPrivileges()){
				
				//一般用户的权限
				if(privilege.getName().equals(privilegeName)){
					return true;
				}
			}
		}
		
		return false;
	}
	public boolean isAdmin(){
		
		return "admin".equals(loginName);
	}
	
	//判断本用户是否有指定URL的权限
	public boolean hasPrivilegeByUrl(String privilegeUrl){
		//超级管理员权限
		if(isAdmin()){
			return true;
		}
		// 如果以UI后缀结尾，就去掉UI后缀，以得到对应的权限（例如：addUI与add是同一个权限）
		if(privilegeUrl.endsWith("UI")){
			privilegeUrl = privilegeUrl.substring(0, privilegeUrl.length()-2);
		}
		//从application域中取出所有的权限
		List<Privilege>allPrivilegeUrls = (List<Privilege>)ActionContext.getContext().getApplication().get("allPrivilegeUrls");
		if(!allPrivilegeUrls.contains(privilegeUrl)){
			//如果是不需要控制功能，所有用户都能访问,如主页和登陆页面
			return true;
		}else{
			//需要控制的功能，需要权限才能访问
			//通过set和get方法得到了user的roles
			for(Role role : roles){
				//角色有哪些权限
				for(Privilege privilege : role.getPrivileges()){
					//一般用户的权限
					if(privilegeUrl.equals(privilege.getUrl())){ //privilege.getUrl()放在里面防止空指针异常
						return true;
					}
				}
			}
		}
		return false;
	}
}
