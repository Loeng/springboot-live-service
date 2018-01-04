package com.sgc.app.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.sgc.domain.shop.Banner;

public interface BannerService extends IService<Banner> {

	List<Banner> getBanner();



}
