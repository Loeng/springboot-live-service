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
@TableName("ims_app_dynamic")
@Data
@EqualsAndHashCode(callSuper = false)
public class Dynamic extends Model<Dynamic> implements Serializable {
	private static final long serialVersionUID = -1990180955471293675L;
	private String id; // varchar(32) NOT NULL,
	private Long userid; //  用户id
	private String content; // 发布文字内容
	private String url;// 图片地址
	private Long time;//發佈時間
	protected Serializable pkVal() {
		return this.id;
	}
}

