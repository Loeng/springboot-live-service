package com.sgc.app.controller.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.mapper.NavsMapper;
import com.sgc.domain.shop.Navs;
import com.sgc.app.service.NavsService;

@Service
public class NavsServiceImpl extends ServiceImpl<NavsMapper, Navs> implements NavsService {
	@Autowired
	private NavsMapper navsMapper;
	@Override
	public List<Navs> getNavs() {
		EntityWrapper<Navs> wrapper = new EntityWrapper<>();
		List<Navs> selectList = navsMapper.selectList(wrapper);
		return selectList;
	}
	
}
