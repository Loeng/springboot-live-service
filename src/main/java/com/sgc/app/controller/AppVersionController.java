package com.sgc.app.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sgc.domain.app.AppVersion;
import com.sgc.domain.vm.ResultVM;
import com.sgc.app.service.AppVersionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("appVersion")
@RequestMapping("/AppVersion")
@Api(value = "app - AppVersion")
public class AppVersionController {
	@Autowired
	private AppVersionService appVersionService;

	/**
	 * 比较App新版本
	 * author EddieAppVersion appVersion  @RequestParam("appVersion") String appVersion
	 */
	@ApiOperation(value = "比较App新版本", httpMethod = "Post", response = ResultVM.class)
	@PostMapping("/updateAppVersion")
	public ResultVM updateAppVersion( @RequestBody JSONObject JSONObject) {
		String appVersion = (String) JSONObject.get("appVersion");
		System.out.println(appVersion);
		//获得app当前版本
		double NowAppVersion = Double.parseDouble(appVersion);
		
		EntityWrapper<AppVersion> wrapper = new EntityWrapper<>();
		wrapper.orderBy("appVersion", false);
		List<AppVersion> list = appVersionService.selectList(wrapper);
		for (AppVersion appVersion2 : list) {
			System.out.println(appVersion2.getAppversion());
		}
		HashMap<String, Object> map = new HashMap<>();
		if(NowAppVersion<list.get(0).getAppversion()) {
			map.put("appVersion", list.get(0));
			map.put("state", "0");
			map.put("msg", "发现更新");
			return ResultVM.ok(map);
		}else {
			map.put("state", "1");
			map.put("msg", "已经是最新版本");
			return ResultVM.ok(map);
		}
		
	}
}









