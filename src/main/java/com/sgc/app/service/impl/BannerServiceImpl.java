package com.sgc.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.mapper.BannerMapper;
import com.sgc.domain.shop.Banner;
import com.sgc.app.service.BannerService;

@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {
	@Autowired
	private BannerMapper bannerMapper;
	@Override
	public List<Banner> getBanner() {
		EntityWrapper<Banner> wrapper = new EntityWrapper<>();
		wrapper.where("uniacid={0}",2);
		List<Banner> selectList = bannerMapper.selectList(wrapper);
		return selectList;
	}
	
}
