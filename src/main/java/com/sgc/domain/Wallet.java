package com.sgc.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("ims_ewei_shop_member_wallet")
@Data
@EqualsAndHashCode(callSuper = false)
public class Wallet {
	private String id;
	private String walletAddress;
	private String username;
	private double amount;
	private Date operateTime;
	private Integer state;
	protected Serializable pkVal() {
		return this.id;
	}
}
