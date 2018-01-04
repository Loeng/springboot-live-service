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
@TableName("ims_ewei_shop_member_friend")
@Data
@EqualsAndHashCode(callSuper = false)
public class Friend extends Model<Friend> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String username;// 申请人
	private String friendname;// 被申请人
	private Date creattime;// 创建时间
	private String type;// 0好友 1申请人拉黑被申请人  2被申请人拉黑申请人  3双方拉黑

	protected Serializable pkVal() {
		return this.id;
	}

}
