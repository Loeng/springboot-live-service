package com.sgc.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * author Eddie
 */
@TableName("ims_ewei_shop_member_address")
@Data
@EqualsAndHashCode(callSuper = false)
public class Address extends Model<Address> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String username;// 地址所属用户名
	private String realname;// 收件人
	private String mobile;//收件人电话
	private String province;// 省
	private String city;// 市
	private String area;// 区
	private String address;// 详细地址
	private Integer isdefault;// 是否为默认地址

	protected Serializable pkVal() {
		return this.id;
	}

}
