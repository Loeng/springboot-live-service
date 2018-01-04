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
@TableName("ims_ewei_shop_goods")
@Data
@EqualsAndHashCode(callSuper = false)
public class Goods extends Model<Goods> implements Serializable {
	private static final long serialVersionUID = -1990180955471293675L;
	private Integer id; // varchar(32) NOT NULL,
	private Integer isrecommand; //1
	private String thumb; //图片
	private String title;//商品姓名
	private double productprice;//商品原价格
	private double marketprice;//市场价格
	protected Serializable pkVal() {
		return this.id;
	}
}

