package com.sgc.app.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.sgc.domain.shop.Category;

public interface CategoryService extends IService<Category> {

	List<Category> getCategory();
}
