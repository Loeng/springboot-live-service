package com.sgc.domain.shop;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("ims_ewei_shop_merch_reg")
@Data
@EqualsAndHashCode(callSuper = false)
public class ShopEnter {
	private static final long serialVersionUID = -1990180955471293675L;
	private Long id; // varchar(32) NOT NULL,
	private String photos;//
	private String idnum;//身份证号码
	private String realname;//姓名
	private String mobile;//手机号
	private Integer status;//申请状态 0审核通过  1审核中  2审核失败
	private String username;
	protected Serializable pkVal() {
		return this.id;
	}
}
