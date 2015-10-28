package com.huawei.oa.base;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.huawei.oa.cfg.Configuration;
import com.huawei.oa.domain.PageBean;
import com.huawei.oa.utils.HqlHelper;

/**
 * 下面的註解代表去掉代碼下面的黃線，其實是為了美觀
 * @author Administrator
 * 這個抽象類目的：很多類都需要實現BaseDao中的方法，通過這個類，其他類只需要繼承這個類，
 * 	就可以使用BaseDao中的方法了，無須自已去實現
 *		BaseDaoImpl<T>与BaseDao<T>：在类上用泛型代表在整个类中都有效
 *				因为实体类继承了BaseDaoImpl类，那么不知道哪个实体类继承它，所以用泛型
 * @param <T>
 */
//@Transactional注解可以被继承，即对子类也有效
@Transactional
@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<T> implements BaseDao<T> {


	/**
	 * @Resource表示注入sessionFactory
	 */
	@Resource
	private SessionFactory sessionFactory;
	/**
	 * 為了让代码做到重用，clazz表示接受一个真正的dao类
	 */
	protected Class<T> clazz;

	/**
	 * this.getClass():传进来的dao对象
	 * 					【因为abstract class BaseDaoImpl<T> implements BaseDao<T>，是抽象类，说明不能创建对象，所以这是子类的对象】
	 * getGenericSuperclass()：表示获取泛型的父类：
	 * 						通过反射获取当前类表示的实体（类，接口，基本类型或void）的直接父类的Type
	 * getActualTypeArguments()[0]：获取第一个泛型参数，因为泛型可以有多个，如BaseDaoImpl<T，E..>,【0】代表第一个 
	 */
	public BaseDaoImpl() {
		ParameterizedType pt = (ParameterizedType) this.getClass()//
				.getGenericSuperclass();
		this.clazz = (Class) pt.getActualTypeArguments()[0];
	}

	@Override
	public void save(T entity) {
		getSession().save(entity);
	}

	/**
	 * 很好奇为啥不能直接getSession().delete(entityId);
	 */
	@Override
	public void delete(Long entityId) {
		Object obj = getSession().get(clazz, entityId);
		getSession().delete(obj);
	}

	@Override
	public void update(T entity) {
		getSession().update(entity);
	}

	/**
	 * 如果传进来的id为空就返回null
	 * (non-Javadoc)
	 * @see com.huawei.oa.base.BaseDao#getById(java.lang.Long)
	 */
	@Override
	public T getById(Long entityId) {
		if(entityId == null){
			return null;
		}
		T entity = (T) getSession().get(clazz, entityId);
		return entity;
	}

	/**
	 * 根据多个id查询
	 * clazz.getSimpleName()：表名
	 */
	@Override
	public List<T> getByIds(Long[] entityIds) {
		if(entityIds == null || entityIds.length == 0){
			return Collections.EMPTY_LIST;//真实的开发情景，返回null集合【emptyList()】的优雅写法
		}
		return getSession()
				.createQuery(
						"FROM " + clazz.getSimpleName() + " WHERE id IN(:ids)")//
				.setParameterList("ids", entityIds)//
				.list();
	}

	@Override
	public List<T> findAll() {

		return getSession().createQuery("FROM " + clazz.getSimpleName() + "")
				.list();
	}

	public Session getSession() {
		Session session = sessionFactory.getCurrentSession();
		return session;
	}
	/**
	 * 分页的公用方法
	 */
	@Override
	public PageBean getPageBean(int pageNum, HqlHelper hqlHelper) {
		int pageSize = Configuration.getPageSize();
		//获取参数
		List<Object> params = hqlHelper.getParameters();
		
		//获取分页的数据列表
		Query listQuery = getSession().createQuery(hqlHelper.getQueryListHql());
		
		//迭代参数，匹配参数.
		//判断有没有带参数过来
		if(params.size() > 0 && params != null){
			for(int i=0; i<params.size();i++){
				listQuery.setParameter(i, params.get(i));
			}
		}
		listQuery.setFirstResult((pageNum-1) * pageSize);
		listQuery.setMaxResults(pageSize);
		List recordList = listQuery.list(); //执行查询
		
		//获取分页的总记录数
		Query CountQuery = getSession().createQuery(hqlHelper.getQueryCountHql());
		if(params.size() > 0 && params != null){
			for(int i=0; i<params.size();i++){
				CountQuery.setParameter(i, params.get(i));
			}
		}
		Long recordCount = (Long) CountQuery.uniqueResult();
		return new PageBean(pageNum, pageSize, recordList, recordCount.intValue());
	}
}
