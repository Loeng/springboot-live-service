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
@TableName("ims_ewei_shop_member_friend_remarks")
@Data
@EqualsAndHashCode(callSuper = false)
public class RemarksFriend extends Model<RemarksFriend> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String remarkUser;// 备注设置人
	private String beRemarkUser;// 被备注人
	private String describe;//描述
	private String remark;// 备注
	private String mobile;//手机

	protected Serializable pkVal() {
		return this.id;
	}

}
