package com.sgc.api.huanxin.impl;

import com.sgc.api.huanxin.AuthTokenAPI;
import com.sgc.comm.TokenUtil;

public class EasemobAuthToken implements AuthTokenAPI{
	

	@Override
	public Object getAuthToken(){
		return TokenUtil.getAccessToken();
	}
}
