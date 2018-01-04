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
@TableName("ims_ewei_app_feedback")
@Data
@EqualsAndHashCode(callSuper = false)
public class FeedBack extends Model<FeedBack> implements Serializable {
	private static final long serialVersionUID = -1990180955471293675L;
	private String id; // varchar(32) NOT NULL,
	private int type; // tinyint(1) COMMENT '反馈类型 1功能异常 2体验问题 3功能建议 4其他',
	private String remark; // varchar(1000) DEFAULT '' COMMENT '备注',
	private Date createtime; // int(11) DEFAULT '0' COMMENT '建立时间',
	private String imageUrl;// 图片地址
	private String status;//0已查看 1未查看
	protected Serializable pkVal() {
		return this.id;
	}
}

