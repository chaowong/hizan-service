package com.se.service.impl;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.se.dao.CategoryDao;
import com.se.dao.base.BaseDao;
import com.se.entity.Category;
import com.se.service.CategoryService;
import com.se.service.base.impl.BaseServiceImpl;
@Service
public class CategoryServiceImpl extends BaseServiceImpl<Category> implements CategoryService{
	@Resource
	private CategoryDao categoryDao;	
	public BaseDao<Category> getBaseDao() {
		return this.categoryDao;
	}
}
