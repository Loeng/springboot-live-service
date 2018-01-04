package com.sgc.app.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.BlackList;
import com.sgc.domain.mapper.BlackListMapper;
import com.sgc.app.service.BlackListService;

@Service
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackList> implements BlackListService {
	

}
