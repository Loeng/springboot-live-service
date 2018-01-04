package com.sgc.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sgc.domain.app.UserProtocol;
import com.sgc.domain.vm.ResultVM;
import com.sgc.app.service.UserProtocolService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("userProtocol")
@RequestMapping("/UserProtocol")
@Api(value = "app - UserProtocol")
public class UserProtocolController {
	@Autowired
	private UserProtocolService userProtocolService;

	/**
	 * 获得用户协议
	 * author EddieAppVersion appVersion  @RequestParam("AppPresent") String AppPresent
	 */
	@ApiOperation(value = "获得用户协议", httpMethod = "Post", response = ResultVM.class)
	@GetMapping("/getUserProtocol")
	public ResultVM getUserProtocol( ) {
		UserProtocol selectOne = userProtocolService.selectOne(null);
		System.out.println(selectOne.getUserprotocol());
		return ResultVM.ok(selectOne);
	
	}
}









