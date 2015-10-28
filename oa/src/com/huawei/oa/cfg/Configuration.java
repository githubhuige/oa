package com.huawei.oa.cfg;

//加载页面的页面大小（pageSize）
public class Configuration {

	private static int pageSize;
	static{
		pageSize = 2;
	}
	
	//供整个工程获取pageSize
	public static int getPageSize() {
		return pageSize;
	}
	 
}
