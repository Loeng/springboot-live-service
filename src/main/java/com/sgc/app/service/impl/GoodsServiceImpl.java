package com.sgc.app.controller.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.mapper.GoodsMapper;
import com.sgc.domain.shop.Goods;
import com.sgc.app.service.GoodsService;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
	@Autowired
	private GoodsMapper goodsMapper;
	@Override
	public List<Goods> getGoods() {
		
		
		EntityWrapper<Goods> entityWrapper = new EntityWrapper<Goods>();
		entityWrapper.where("isrecommand={0}",1)
				.andNew("deleted={0}",0)
				.andNew("status>{0}",0);


		 List<Goods> selectPage = goodsMapper.selectPage(new Page<Goods>(0, 4), entityWrapper);
		return selectPage;
	}
	
	
	@Override
	public List<Goods> getGoodsPage(Integer page ) {
		
		
		EntityWrapper<Goods> entityWrapper = new EntityWrapper<Goods>();
		entityWrapper.where("isrecommand={0}",1)
				.andNew("deleted={0}",0)
				.andNew("status>{0}",0);

		 List<Goods> selectPage = goodsMapper.selectPage(new Page<Goods>(page,4), entityWrapper);
		return selectPage;
	}
}
