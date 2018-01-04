package com.sgc.web.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sgc.domain.shop.Banner;
import com.sgc.domain.shop.Category;
import com.sgc.domain.shop.Goods;
import com.sgc.domain.shop.Navs;
import com.sgc.domain.vm.ResultVM;
import com.sgc.app.service.BannerService;
import com.sgc.app.service.CategoryService;
import com.sgc.app.service.GoodsService;
import com.sgc.app.service.NavsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by Eddie
 */
@Api(value = "API - HomeController")
@RestController("home")
@RequestMapping("/home")
public class HomeController {
			@Autowired
			private CategoryService categoryService;
			@Autowired
			private BannerService bannerService;
			@Autowired
			private NavsService navsService;
			@Autowired
			private GoodsService goodsService;
	@ApiOperation(value = "首页展示", httpMethod = "Get", response = ResultVM.class)
	@GetMapping("/index")
	public ResultVM index() {
		//类目
		List<Category> categoryList = categoryService.getCategory();
		//轮播图
		List<Banner> BannerList = bannerService.getBanner();
		//导航栏
		List<Navs> NavsList = navsService.getNavs();
		//猜你喜欢
		List<Goods> GoodsList = goodsService.getGoods();
		HashMap<String, Object> map = new HashMap<>();
		map.put("categoryList", categoryList);
		map.put("BannerList", BannerList);
		map.put("NavsList", NavsList);
		map.put("GoodsList", GoodsList);
				return ResultVM.ok(map);
	}
	
	@ApiOperation(value = "首页展示", httpMethod = "Get", response = ResultVM.class)
	@GetMapping("/index/like/{page}")
	public ResultVM getLikePage(
			@PathVariable("page")Integer page) {
		List<Goods> goodsPage = goodsService.getGoodsPage(page);
				return ResultVM.ok(goodsPage);
	}
	
	
	
}
