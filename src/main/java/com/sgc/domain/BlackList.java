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
@TableName("ims_ewei_shop_member_black_friend")
@Data
@EqualsAndHashCode(callSuper = false)
public class BlackList extends Model<BlackList> implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String username;// 用户名
	private String  blackname;// 被拉黑用户名
	private Date creattime;// 创建时间

	protected Serializable pkVal() {
		return this.id;
	}

}
