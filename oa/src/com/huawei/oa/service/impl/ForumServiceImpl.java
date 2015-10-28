package com.huawei.oa.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.huawei.oa.base.BaseDaoImpl;
import com.huawei.oa.domain.Forum;
import com.huawei.oa.service.ForumService;

@Service
@SuppressWarnings("unchecked")
public class ForumServiceImpl extends BaseDaoImpl<Forum> implements ForumService{

	/**
	 * 思路：
	 * 	1通过设置position属性与id同步。这样可以通过id代表position
	 * 	2要想上移就必须知道它上面的id是哪个
	 */
	//显示时以升序显示，重写base层的findAll方法
	@Override
	public List<Forum> findAll() {
		return getSession().createQuery(//
				"FROM Forum f ORDER BY f.position ASC")//
				.list();
	}
	//通过设置position属性与id同步。这样可以通过id代表position 
	//重写base层的save方法
	@Override
	public void save(Forum entity) {
		//保存forum 到数据库中
		getSession().save(entity);
		//因为id 为Long类型，而position为int类型，所以需要类型转换
		entity.setPosition(entity.getId().intValue());
		//因为是持久化状态，所以不需要在update(entity)了
		
	}
	

	@Override
	public void moveUp(Long id) {
		
		// 获取要交换的两个Forum
		//当前操作的position
		Forum forum = getById(id);
		//获取上面的position
		Forum other = (Forum) getSession().createQuery(//
				"FROM Forum f WHERE f.position<? ORDER BY f.position DESC")// 
				.setParameter(0, forum.getPosition())//上面的位置，因为是升序排的。所以当前操作的位置在下面
				.setFirstResult(0)//获取上面的位置，因为有很多个，所以按降序排，然后取最大的一个，每页从第一个开始
				.setMaxResults(1)//每一页取一个
				.uniqueResult();
		//最上面的不能上移
		if(other == null){
			return;
		}
		
		//交换
		int temp = forum.getPosition();
		forum.setPosition(other.getPosition());
		other.setPosition(temp);
		//因为是持久化状态，所以不需要在update(entity)了
	}

	//下移
	@Override
	public void moveDown(Long id) {
		// 获取要交换的两个Forum
		//当前操作的position
		Forum forum = getById(id);
		//获取上面的position
		Forum other = (Forum) getSession().createQuery(//
				"FROM Forum f WHERE f.position>? ORDER BY f.position ASC")// 
				.setParameter(0, forum.getPosition())//上面的位置，因为是升序排的。所以当前操作的位置在上面
				.setFirstResult(0)//获取上面的位置，因为有很多个，所以按升序排，然后取最大的一个，每页从第一个开始
				.setMaxResults(1)//每一页取一个
				.uniqueResult();
		//最下面的不能下移
		if(other == null){
			return;
		}
		//交换
		int temp = forum.getPosition();
		forum.setPosition(other.getPosition());
		other.setPosition(temp);
		//因为是持久化状态，所以不需要在update(entity)了
	}

}
