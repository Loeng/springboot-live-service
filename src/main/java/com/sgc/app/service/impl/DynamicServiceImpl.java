package com.sgc.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.Dynamic;
import com.sgc.domain.mapper.DynamicMapper;
import com.sgc.app.service.DynamicService;

@Service
public class DynamicServiceImpl extends ServiceImpl<DynamicMapper, Dynamic> implements DynamicService {
	@Autowired
	private DynamicMapper dynamicMapper;


}
