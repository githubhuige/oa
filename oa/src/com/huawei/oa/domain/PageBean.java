package com.huawei.oa.domain;

import java.util.List;

public class PageBean {
	//通过传递过来的参数或者配置的参数
	private int currentPage;//当前页面
	private int pageSize;//每页显示多少条记录
	
	//通过查询数据库获取到的
	private List recordList;//本页的数据列表
	private int recordCount;//总记录数
	 
	//通过计算获取到的
	private int pageCount;//总页数
	private int beginPageIndex;//页码列表的开始索引
	private int endPageIndex;//页码列表的结束索引
	
	
	public PageBean(int currentPage, int pageSize, List recordList,
			int recordCount) {
		super();
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.recordList = recordList;
		this.recordCount = recordCount;
		//总页数
		pageCount = (recordCount + pageSize-1) / pageSize;
		//页码列表的开始索引
		//页码列表的结束索引
		//>>总页数小于或者等于10
		if(pageCount <= 10){
			beginPageIndex = 1;
			endPageIndex = pageCount;
		}else{
		//>> 总页码大于10页时，就只显示当前页附近的共10个页码
			// 默认显示 前4页 + 当前页 + 后5页
			beginPageIndex = currentPage - 4;
			endPageIndex = currentPage + 5;
			//如果前面不足4个，则显示前10个
			if(beginPageIndex < 1){
				beginPageIndex = 1;
				endPageIndex = 10;
			}else if(endPageIndex > pageCount){
				//如果后面不足5个，则显示后面10个
				beginPageIndex = pageCount - 9;
				endPageIndex = pageCount;
			}
		}
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public List getRecordList() {
		return recordList;
	}
	public void setRecordList(List recordList) {
		this.recordList = recordList;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getBeginPageIndex() {
		return beginPageIndex;
	}
	public void setBeginPageIndex(int beginPageIndex) {
		this.beginPageIndex = beginPageIndex;
	}
	public int getEndPageIndex() {
		return endPageIndex;
	}
	public void setEndPageIndex(int endPageIndex) {
		this.endPageIndex = endPageIndex;
	}

}
