package com.sgc.app.controller.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.Currency;
import com.sgc.domain.mapper.CurrencyMapper;
import com.sgc.app.service.CurrencyService;

@Service
public class CurrencyServiceImpl extends ServiceImpl<CurrencyMapper, Currency> implements CurrencyService {
	@Autowired
	private CurrencyMapper currencyMapper;

	
}
