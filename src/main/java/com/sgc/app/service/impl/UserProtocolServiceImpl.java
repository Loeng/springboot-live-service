package com.sgc.app.controller.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.app.UserProtocol;
import com.sgc.domain.mapper.UserProtocolMapper;
import com.sgc.app.service.UserProtocolService;

@Service
public class UserProtocolServiceImpl extends ServiceImpl<UserProtocolMapper, UserProtocol> implements UserProtocolService {
	@Autowired
	private UserProtocolMapper userProtocolMapper;
	
	
}
