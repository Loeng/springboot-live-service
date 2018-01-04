package com.sgc.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.Friend;
import com.sgc.domain.mapper.FriendMapper;
import com.sgc.app.service.FriendService;

@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {
	@Autowired
	private FriendMapper friendMapper;

	
}
