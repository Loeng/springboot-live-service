package com.sgc.domain;


import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Bruin
 */
@TableName("sys_user")
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUser extends BaseModel<SysUser> {
	private static final long serialVersionUID = -408628804715313592L;
	private String username;
	private String password;
	private String email;
	private String mobile;
	private String status;
	@TableField(exist = false)
	private List<SysRole> rolelist;

	public SysUser(String username) {
		this.username = username;
	}

	public SysUser() {
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
