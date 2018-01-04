package com.sgc.domain;

import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.sgc.domain.vm.JsTreeVM;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Bruin
 */
@TableName("sys_role")
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRole extends BaseModel<SysRole> {
	private static final long serialVersionUID = -5543764845240092138L;
	private String roleName;
	private String remark;
	@TableField(exist = false)
	private Boolean checked;
	@TableField(exist = false)
	private List<JsTreeVM> menuTree;

	public SysRole() {
	}

	public SysRole(String roleName) {
		this.roleName = roleName;
	}
}
