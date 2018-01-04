package com.sgc.app.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.sgc.domain.shop.Goods;

public interface GoodsService extends IService<Goods> {

	List<Goods> getGoods();

	List<Goods> getGoodsPage(Integer page );

}
