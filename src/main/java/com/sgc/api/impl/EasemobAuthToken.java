package com.sgc.api.impl;

import com.sgc.api.AuthTokenAPI;
import com.sgc.comm.TokenUtil;

public class EasemobAuthToken implements AuthTokenAPI{
	

	@Override
	public Object getAuthToken(){
		return TokenUtil.getAccessToken();
	}
}
