package com.sgc.app.service.impl;

import com.sgc.app.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.Wallet;
import com.sgc.domain.mapper.WalletMapper;

@Service
public class WalletServiceImpl extends ServiceImpl<WalletMapper, Wallet> implements WalletService {
	@Autowired
	private WalletMapper walletMapper;

	
}
