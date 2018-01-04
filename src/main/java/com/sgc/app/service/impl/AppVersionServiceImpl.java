package com.sgc.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.app.AppVersion;
import com.sgc.domain.mapper.AppVersionMapper;
import com.sgc.app.service.AppVersionService;

@Service
public class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersion> implements AppVersionService {
	@Autowired
	private AppVersionMapper appVersionMapper;
	
	
}
