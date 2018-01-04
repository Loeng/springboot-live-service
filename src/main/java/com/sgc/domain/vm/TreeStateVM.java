package com.sgc.domain.vm;

import java.io.Serializable;

/**
 * Created by Bruin
 */
public class TreeStateVM implements Serializable {
	private static final long serialVersionUID = 1L;
	private Boolean checked;

	private Boolean expand;

	private Boolean disabled;


	public TreeStateVM() {
		super();
	}

	public TreeStateVM(Boolean checked) {
		super();
		this.checked = checked;
	}

	public TreeStateVM(Boolean checked, Boolean disabled) {
		super();
		this.checked = checked;
		this.disabled = disabled;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	

	public Boolean getExpand() {
		return expand;
	}

	public void setExpand(Boolean expand) {
		this.expand = expand;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
}
