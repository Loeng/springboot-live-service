package com.sgc.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.FriendApply;
import com.sgc.domain.mapper.FriendApplyMapper;
import com.sgc.app.service.FriendApplyService;

@Service
public class FriendApplyServiceImpl extends ServiceImpl<FriendApplyMapper, FriendApply> implements FriendApplyService {
	@Autowired
	private FriendApplyMapper friendApplyMapper;

	
}
