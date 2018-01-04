package com.sgc.app.controller.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.mapper.CategoryMapper;
import com.sgc.domain.shop.Category;
import com.sgc.app.service.CategoryService;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public List<Category> getCategory() {
		EntityWrapper<Category> wrapper = new EntityWrapper<>();
		wrapper.where("uniacid={0}",2);
		List<Category> selectList = categoryMapper.selectList(wrapper);
		return selectList;
		
	}

	
}
