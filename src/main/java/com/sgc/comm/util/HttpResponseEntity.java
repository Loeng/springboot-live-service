package com.sgc.comm.util;
/**
 * HttpResponse响应实体
 * @author LEE.SIU.WAH
 * @email lixiaohua7@163.com
 * @date 2017年8月29日 上午11:36:28
 * @version 1.0
 */
public class HttpResponseEntity {
	/** 状态码 */
	private int statusCode;
	/** 响应数据 */
	private String responseData;
	public HttpResponseEntity(){};
	public HttpResponseEntity(int statusCode, String responseData) {
		this.statusCode = statusCode;
		this.responseData = responseData;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getResponseData() {
		return responseData;
	}
	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}
}