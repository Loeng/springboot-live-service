package com.sgc.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.FeedBack;
import com.sgc.domain.mapper.FeedBackMapper;
import com.sgc.app.service.FeedBackService;

@Service
public class FeedBackServiceImpl extends ServiceImpl<FeedBackMapper, FeedBack> implements FeedBackService {
	@Autowired
	private FeedBackMapper feedBackMapper;

	
}
