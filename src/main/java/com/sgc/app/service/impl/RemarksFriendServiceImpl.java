package com.sgc.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.RemarksFriend;
import com.sgc.domain.mapper.RemarksFriendMapper;
import com.sgc.app.service.RemarksFriendService;

@Service
public class RemarksFriendServiceImpl extends ServiceImpl<RemarksFriendMapper, RemarksFriend> implements RemarksFriendService {
	@Autowired
	private RemarksFriendMapper remarksFriendMapper;

	
}
