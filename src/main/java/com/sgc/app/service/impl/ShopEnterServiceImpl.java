package com.sgc.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.mapper.ShopEnterMapper;
import com.sgc.domain.shop.ShopEnter;
import com.sgc.app.service.ShopEnterService;

@Service
public class ShopEnterServiceImpl extends ServiceImpl<ShopEnterMapper, ShopEnter> implements ShopEnterService {
	@Autowired
	private ShopEnterMapper shopEnterMapper;

}
