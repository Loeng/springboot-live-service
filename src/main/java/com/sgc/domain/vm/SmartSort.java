package com.sgc.domain.vm;

import java.io.Serializable;


public class SmartSort implements Serializable {
	private static final long serialVersionUID = -2023024317939176197L;

	private String predicate;

	private boolean reverse;

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public boolean getReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
}
