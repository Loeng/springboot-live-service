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
@TableName("ims_ewei_shop_merch_category")
@Data
@EqualsAndHashCode(callSuper = false)
public class Category extends Model<Category> implements Serializable {
	private static final long serialVersionUID = -1990180955471293675L;
	private Integer id; // varchar(32) NOT NULL,
	private Integer uniacid; 
	private String catename; //类名
	private String icon; //圖片
	private String en;//英文名
	private String url;//跳轉url
	protected Serializable pkVal() {
		return this.id;
	}
}

