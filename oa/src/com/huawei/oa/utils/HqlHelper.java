package com.huawei.oa.utils;

import java.util.ArrayList;
import java.util.List;

import com.huawei.oa.base.BaseDao;
import com.huawei.oa.domain.PageBean;
import com.opensymphony.xwork2.ActionContext;

/**
 * 用于拼接hql语句
 * @author Administrator
 *
 */
public class HqlHelper {
	private String fromClause;//form子句，必须有
	private String whereClause = "";//where子句，可选,默认为""
	private String orderClause = "";//order by 子句，可选,默认为""
	private List<Object> parameters = new ArrayList<Object>();//用于保存参数列表
	
	//拼接from子句,别名是“o”,利用构造函数进行拼接，说明这个必须要有
	public HqlHelper(Class clazz){
		this.fromClause = "FROM " + clazz.getSimpleName() + " o";
	}
	//拼接from子句,别名是alise
	public HqlHelper(Class clazz,String alias){
		this.fromClause = "FROM " + clazz.getSimpleName() + " " + alias;
	}
	/**
	 * 拼接where子句  【不按精华帖过滤】
	 * condition:过滤的条件
	 * Object... params:过滤匹配的参数【可变参数，按顺序进行匹配】
	 */
	public HqlHelper addCondition(String condition, Object... params){
		//判断有没有where
		//没有where
		if(whereClause.length() == 0){
			this.whereClause = " WHERE " + condition;
		}else{
			this.whereClause += " AND " + condition;
		}
		//保存参数
		//判断是否有参数
		if(params.length >0 && params != null){
			for(Object obj : params){
				parameters.add(obj);
			}
		}
		return this;//返回HqlHelper
	}
	/**
	 * 判断如果是精华帖，那么就采用下面这个方法拼接
	 */
	public HqlHelper addCondition(boolean apend,String condition, Object... params){
		if(apend){
			addCondition(condition, params);
		}
		return this;//返回HqlHelper
	}
	/**
	 * 拼接order by子句
	 * propertyName
	 *            属性名
	 * @param isAsc
	 *            true表示升序，false表示降序
	 */
	public HqlHelper addOrder(String propertyName,boolean isAsc){
		//判断
		if(orderClause.length() == 0){
			this.orderClause = " ORDER BY " + propertyName + (isAsc ? " ASC" : " DESC");
		}else{
			this.orderClause += ", " +propertyName + (isAsc ? " ASC" : " DESC");
		}
		
		return this;
	}
	/**
	 * 拼接order by子句
	 * propertyName
	 *            属性名
	 * @param isAsc
	 *            true表示升序，false表示降序
	 *            
	 */
	public HqlHelper addOrder(boolean apend,String propertyName,boolean isAsc){
		if(apend){
			addOrder(propertyName, isAsc);
		}
		
		return this;
	}
	/**
	 * 获取生成的查询数据列表的HQL语句
	 */
	public String getQueryListHql(){
		
		return fromClause + whereClause + orderClause;
	}
	/**
	 * 获取生成的总记录数的HQL语句
	 */
	public String getQueryCountHql(){
		
		return "SELECT COUNT(*) " + fromClause + whereClause;
	}
	/**
	 * 获取参数列表，与HQL过滤条件中的'?'一一对应
	 * @return
	 */
	public List<Object> getParameters() {
		return parameters;
	}
	/**
	 * 查询并准备分页信息（放到栈顶）
	 * 
	 * @param pageNum
	 * @param service：通过传进来的service.可以知道哪个需要分页
	 * @return
	 */
	public HqlHelper buildPageBeanForStruts2(int pageNum, BaseDao<?> service){
		//通过传一个要查看哪一页的数据，和hql语句以及hql的需要的参数【this包含两者：HqlHelper】
		PageBean pageBean = service.getPageBean(pageNum,this);
		ActionContext.getContext().getValueStack().push(pageBean);
		return this;//返回HqlHelper
	}

}
