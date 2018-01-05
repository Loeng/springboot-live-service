package com.sgc.domain.vm;

import java.io.Serializable;


public class JsTreeVM implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String icon;
    private TreeStateVM state;
    private String parent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public TreeStateVM getState() {
        return state;
    }

    public void setState(TreeStateVM state) {
        this.state = state;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parentId) {
        this.parent = parentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String type) {
        this.icon = type;
    }
}
