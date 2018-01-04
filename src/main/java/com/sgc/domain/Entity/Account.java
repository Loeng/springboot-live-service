package com.sgc.domain.Entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 类说明:用户实体类
 *
 * @author Wvv
 */
@TableName("live_account")
@Data
@EqualsAndHashCode(callSuper = false)
public class Account extends Model<Account> implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String uid;
	private String pwd;
	private String token;
	private Integer state;
	private String sig;
	private String create_time;
	private String login;
	private String loginout;
	private String login_last;


	public Account(){}
	public Account(String uid,String pwd) {
		this.uid = uid;
		this.pwd=pwd;
	}

	protected Serializable pkVal() {
		return this.uid;
	}

}
