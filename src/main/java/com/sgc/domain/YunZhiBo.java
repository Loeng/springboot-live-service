package com.sgc.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class YunZhiBo {
	private String id;
	private String pwd;
	private String appId;
	
}
