package com.sgc.app.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.sgc.domain.shop.Navs;

public interface NavsService extends IService<Navs> {

	List<Navs> getNavs();
}
