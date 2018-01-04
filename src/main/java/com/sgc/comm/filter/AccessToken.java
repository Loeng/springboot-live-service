package com.sgc.comm.filter;

import com.sgc.domain.User;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AccessToken {
	private String access_token;  
    private String token_type;//类型
    private long expires_in;//token时长
    private User user;
    private String LiveToken;//直播间Token
    private String userSig; //直播间签名
}
