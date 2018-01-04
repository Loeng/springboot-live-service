package com.sgc.domain.vm;

import java.io.Serializable;

/**
 * Created by Bruin
 */
public class DynamicVM implements Serializable {
	@Override
	public String toString() {
		return "DynamicVM [id=" + id + ", nickname=" + nickname + ", avatar=" + avatar + ", content=" + content
				+ ", url=" + url + ", time=" + time + "]";
	}
	private String id; // varchar(32) NOT NULL,
	private String nickname;//昵称
	private String avatar;//头像 	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public DynamicVM() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DynamicVM(String id, String nickname, String avatar, String content, String url, Long time) {
		super();
		this.id = id;
		this.nickname = nickname;
		this.avatar = avatar;
		this.content = content;
		this.url = url;
		this.time = time;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	private String content; // 发布文字内容
	private String url;// 图片地址
	private Long time;//發佈時間
}
