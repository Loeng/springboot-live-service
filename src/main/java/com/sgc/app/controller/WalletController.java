package com.sgc.app.controller;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sgc.comm.util.GUIDHelper;
import com.sgc.domain.Wallet;
import com.sgc.domain.vm.ResultVM;
import com.sgc.app.controller.service.WalletService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("appWallet")
@RequestMapping("/wallet")
@Api(value = "app - Wallet")
public class WalletController {
	@Autowired
	private WalletService walletService;
	
	/**
	 * 新增一个钱包
	 * author Eddie
	 * http://localhost:9992/v1/wallet/addWallet
	 */
	@ApiOperation(value = "新增一个钱包", httpMethod = "Post", response = ResultVM.class)
	@PostMapping("/addWallet")
	public ResultVM addWallet(Wallet wallet) {
		wallet.setId(GUIDHelper.genRandomGUID());
		wallet.setOperateTime(new Date());
		boolean insert = walletService.insert(wallet);
		if(insert) {
			return ResultVM.ok("新增钱包成功");
		}else {
			return ResultVM.error(1, "新增钱包失败");
		}
		
	
	}
	
	
	/**
	 * 通过用户名获得钱包信息
	 * author Eddie
	 * http://localhost:9992/v1/wallet/getWallet/{username}
	 */
	@ApiOperation(value = "通过用户名获得钱包信息", httpMethod = "Get", response = ResultVM.class)
	@GetMapping("/getWallet/{username}")
	public ResultVM getWallet(@PathVariable("username") String username) {
		System.out.println(username);
		EntityWrapper<Wallet> wrapper = new EntityWrapper<>();
		wrapper.where("username={0}", username);
		Wallet wallet = walletService.selectOne(wrapper);
		System.out.println(wallet);
		if(wallet!=null) {
			HashMap<String, Object> map = new HashMap<>();
			return ResultVM.ok(wallet);
		}else {
			return ResultVM.error(1,"获取钱包信息失败");
		}
		
		
		
	}
}













