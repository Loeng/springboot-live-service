package com.sgc.domain.app;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * author Eddie
 */
@TableName("ims_app_userprotocol")
@Data
@EqualsAndHashCode(callSuper = false)
public class UserProtocol extends Model<UserProtocol> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String userprotocol;// 用户协议


	protected Serializable pkVal() {
		return this.id;
	}

}
