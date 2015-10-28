package com.huawei.oa.base;

import java.util.List;

import com.huawei.oa.domain.PageBean;
import com.huawei.oa.utils.HqlHelper;

/**
 * 這是一個總接口，封裝了一些公用 的方法
 * @author Administrator
 *
 * @param <T>
 */
public interface BaseDao<T> {

	void save(T entity);

	void delete(Long entityId);

	void update(T entity);

	T getById(Long entityId);

	List<T> getByIds(Long[] entityIds);

	List<T> findAll();

	PageBean getPageBean(int pageNum, HqlHelper hqlHelper);
}
