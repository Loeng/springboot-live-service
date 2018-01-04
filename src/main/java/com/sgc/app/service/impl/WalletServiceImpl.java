package com.sgc.app.controller.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.Wallet;
import com.sgc.domain.mapper.WalletMapper;
import com.sgc.app.controller.service.WalletService;

@Service
public class WalletServiceImpl extends ServiceImpl<WalletMapper, Wallet> implements WalletService {
	@Autowired
	private WalletMapper walletMapper;

	
}
