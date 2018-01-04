package com.sgc.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author felix 2017年11月10日 下午12:37:09 类说明:用户实体类
 */
@TableName("ims_ewei_shop_member")
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends Model<User> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id; 
	private Integer uniacid;
	private String openid;
	private String mobileverify;
	private String comefrom;
	private String salt;
	private String realname; 
	private String mobile; 
	private String pwd; 
	private String nickname;
	private String birthday;
	private String gender; 
	private String avatar; 
	private String province;
	private String city;
	private String area;
	private String username; 
	private Long createtime;
	private String email;
	private String paypwd;//支付密码
	private String auth;// 邮箱验证 0未验证 1验证
	private String messagecall;// 是否消息提醒 0.开启 1.关闭
	private String smscall;// 是否短信提醒 0.开启 1.关闭
	private String isbind;//是否绑定邮箱 0未绑定 1绑定
	private String ispaypwd;//是否修改支付密码 0未修改 1修改
	private String ismobile;//是否绑定手机 0未绑定 1绑定
	private String key;
	protected Serializable pkVal() {
		return this.id;
	}
	

}
