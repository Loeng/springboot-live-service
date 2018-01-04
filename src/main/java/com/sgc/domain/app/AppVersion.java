package com.sgc.domain.app;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("ims_ewei_app_version")
@Data
@EqualsAndHashCode(callSuper = false)
public class AppVersion extends Model<AppVersion> implements Serializable {
	private static final long serialVersionUID = -8203463284440399882L;
	private String id; //varchar(32) NOT NULL,
	private double appversion; //varchar(32) DEFAULT NULL COMMENT '版本号',
	private String apphref; //varchar(256) DEFAULT NULL COMMENT '版本下载链接',
	private String editcontent; //varchar(512) DEFAULT NULL COMMENT '修改内容',
	private String addcontent; //varchar(512) DEFAULT NULL COMMENT '新增内容',
	private Date createtime; //datetime DEFAULT NULL COMMENT '创建时间',
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
