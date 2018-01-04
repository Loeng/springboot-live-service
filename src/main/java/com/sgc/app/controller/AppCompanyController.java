package com.sgc.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sgc.domain.app.AppCompany;
import com.sgc.domain.vm.ResultVM;
import com.sgc.app.service.AppCompanyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("appCompany")
@RequestMapping("/AppCompany")
@Api(value = "app - AppCompany")
public class AppCompanyController {
	@Autowired
	private AppCompanyService appCompanyService;

	/**
	 * 获得公司介绍
	 * author EddieAppVersion appVersion  @RequestParam("AppPresent") String AppPresent
	 */
	@ApiOperation(value = "获得公司介绍", httpMethod = "Get", response = ResultVM.class)
	@GetMapping("/getAppCompany")
	public ResultVM getAppCompany( ) {
		AppCompany selectOne = appCompanyService.selectOne(null);
		System.out.println(selectOne.getCompanypresent());
		return ResultVM.ok(selectOne);
	
	}
}









