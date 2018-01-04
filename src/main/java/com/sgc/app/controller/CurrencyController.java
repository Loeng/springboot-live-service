package com.sgc.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sgc.comm.util.GUIDHelper;
import com.sgc.domain.Currency;
import com.sgc.domain.vm.ResultVM;
import com.sgc.app.service.CurrencyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("appCurrency")
@RequestMapping("/Currency")
@Api(value = "app - Currency")
public class CurrencyController {
	@Autowired
	private CurrencyService currencyService;
	/**
	 * 设置汇率 
	 * author Eddie
	 */
	@ApiOperation(value = "设置汇率 ", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/addCurrency")
	public ResultVM addCurrency(Currency currency){
		currency.setId(GUIDHelper.genRandomGUID());
		boolean insert = currencyService.insert(currency);
		if(insert) {
			return ResultVM.ok("新增货币汇率成功");
		}else {
			return ResultVM.error(1, "新增货币汇率失败");
		}

	}
	
	
	
}













