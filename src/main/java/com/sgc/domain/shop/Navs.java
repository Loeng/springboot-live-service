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
@TableName("ims_ewei_shop_merch_ navs")
@Data
@EqualsAndHashCode(callSuper = false)
public class Navs extends Model<Navs> implements Serializable {
	private static final long serialVersionUID = -1990180955471293675L;
	private String id; // varchar(32) NOT NULL,
	private String name;
	private String src;
	private String url;
	protected Serializable pkVal() {
		return this.id;
	}
}

