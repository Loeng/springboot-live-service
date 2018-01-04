package com.sgc.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("ims_ewei_shop_member_currency")
@Data
@EqualsAndHashCode(callSuper = false)
public class Currency {
	private String id;
	private String currency;
	private String username;
	private double exchangeRate;
	protected Serializable pkVal() {
		return this.id;
	}
}
