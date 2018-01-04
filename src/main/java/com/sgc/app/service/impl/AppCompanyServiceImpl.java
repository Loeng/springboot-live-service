package com.sgc.app.controller.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.app.AppCompany;
import com.sgc.domain.mapper.AppCompanyMapper;
import com.sgc.app.service.AppCompanyService;

@Service
public class AppCompanyServiceImpl extends ServiceImpl<AppCompanyMapper, AppCompany> implements AppCompanyService {
	@Autowired
	private AppCompanyMapper appCompanyMapper;
	
	
}
