package com.sgc.domain.shop;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * author Eddie
 */
@TableName("ims_ewei_shop_merch_adv")
@Data
@EqualsAndHashCode(callSuper = false)
public class Banner extends Model<Banner> implements Serializable {
	private static final long serialVersionUID = -1990180955471293675L;
	private Integer id; // varchar(32) NOT NULL,
	private Integer uniacid;
	private String link;
	private String thumb;
	protected Serializable pkVal() {
		return this.id;
	}
}

