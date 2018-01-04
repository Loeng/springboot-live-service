package com.sgc.comm.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 * Created by xjpan on 2017/11/19.
 */
public class HttpClientService {

	private CloseableHttpClient httpClient;

	public HttpClientService() {
		// 创建httpClient对象
		httpClient = HttpClients.createDefault();
	}

	/**
	 * 不带参数的get方法
	 * 
	 * @param url
	 *            请求地址
	 * @return
	 * @throws Exception
	 */
	public String doGet(String url) throws Exception {
		return doGet(url, null);
	}

	/**
	 * 带参数的get方法
	 * 
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求参数
	 * @return
	 * @throws Exception
	 */
	public String doGet(String url, Map<String, Object> param) throws Exception {

		URIBuilder uriBuilder = new URIBuilder(url);
		if (param != null) {
			Set<Map.Entry<String, Object>> entrySet = param.entrySet();
			for (Map.Entry<String, Object> entry : entrySet) {
				uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
			}
		}

		// 创建httpGet
		HttpGet http = new HttpGet(uriBuilder.build());

		CloseableHttpResponse httpResponse = null;
		try {
			// 执行
			httpResponse = httpClient.execute(http);
			// 处理结果
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String content = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
				return content;
			}
		} finally {
			if (httpResponse != null) {
				httpResponse.close();
			}
			httpClient.close();
		}
		return null;
	}

	/**
	 * 不带参数的post方法
	 * 
	 * @param url
	 *            请求地址 //@param param 请求参数
	 * @return
	 * @throws Exception
	 */
	public HttpResult doPost(String url) throws Exception {
		return doPost(url, null);
	}

	/**
	 * 带参数的post方法
	 * 
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求参数
	 * @return
	 * @throws Exception
	 */
	public HttpResult doPost(String url, Map<String, Object> param) throws Exception {

		// 创建httpPost
		HttpPost http = new HttpPost(url);
		if (param != null) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			Set<Map.Entry<String, Object>> entrySet = param.entrySet();
			for (Map.Entry<String, Object> entry : entrySet) {
				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}

			// 设置参数
			http.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
		}

		CloseableHttpResponse httpResponse = null;
		try {
			// 执行
			httpResponse = httpClient.execute(http);
			// 处理结果
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String content = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
				return new HttpResult(HttpStatus.SC_OK, content);
			}
		} finally {
			if (httpResponse != null) {
				httpResponse.close();
			}
			httpClient.close();
		}
		return new HttpResult(httpResponse.getStatusLine().getStatusCode());
	}

	// RequestConfig config = RequestConfig.custom()
	// .setSocketTimeout(5000)
	// .setConnectTimeout(5000)
	// .setConnectionRequestTimeout(5000)
	// .setStaleConnectionCheckEnabled(true)
	// .build();
	/**
	 * 新加带有header
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public HttpResult doPostNew(String url, Map<String, Object> param, String var1, String var2) throws Exception {

		// 创建httpPost
		HttpPost http = new HttpPost(url);
		http.setHeader(var1, var2);
		if (param != null) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			Set<Map.Entry<String, Object>> entrySet = param.entrySet();
			for (Map.Entry<String, Object> entry : entrySet) {
				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}

			// 把 parameter的类 转为 json格式
			// Gson gson = new Gson();
			// String parameter = gson.toJson(param);
			// http.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
			String text = JSON.toJSONString(param);
			System.out.println(text);
			http.setEntity(new StringEntity(text, "utf-8"));
			http.setHeader("content-Type", "application/json;charset=UTF-8");
		}

		CloseableHttpResponse httpResponse = null;
		try {
			// 执行
			httpResponse = httpClient.execute(http);
			// 处理结果
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String content = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
				return new HttpResult(HttpStatus.SC_OK, content);
			}
		} finally {
			if (httpResponse != null) {
				httpResponse.close();
			}
			httpClient.close();
		}
		return new HttpResult(httpResponse.getStatusLine().getStatusCode());
	}

	/**
	 * 带参数的put方法
	 * 
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求参数
	 * @return
	 * @throws Exception
	 */
	public HttpResult doPut(String url, Map<String, Object> param) throws Exception {

		// 创建httpPut
		HttpPut http = new HttpPut(url);

		if (param != null) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			Set<Map.Entry<String, Object>> entrySet = param.entrySet();
			for (Map.Entry<String, Object> entry : entrySet) {
				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}

			// 设置参数
			http.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
		}

		CloseableHttpResponse httpResponse = null;
		try {
			// 执行
			httpResponse = httpClient.execute(http);
			// 处理结果
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String content = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
				return new HttpResult(HttpStatus.SC_OK, content);
			}
		} finally {
			if (httpResponse != null) {
				httpResponse.close();
			}
			httpClient.close();
		}
		return new HttpResult(httpResponse.getStatusLine().getStatusCode());
	}

	/**
	 * 带参数的delete方法
	 * 
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求参数
	 * @return
	 * @throws Exception
	 */
	public HttpResult doDelete(String url, Map<String, Object> param) throws Exception {
		if (param == null) {
			param = new HashMap<String, Object>();
		}
		param.put("_method", "delete");

		return doPost(url, param);
	}
	public HttpResult doDostWithJSON(String url, String json) throws Exception {
        // 将JSON进行UTF-8编码,以便传输中文
        String encoderJson = URLEncoder.encode(json, HTTP.UTF_8);
        String app_JSON = "application/json";
        String ctx_JSON = "text/json";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, app_JSON);
        
        StringEntity se = new StringEntity(json);
        se.setContentType(ctx_JSON);
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, app_JSON));
        httpPost.setEntity(se);
        
        CloseableHttpResponse response = httpClient.execute(httpPost);
        
        if (response.getStatusLine().getStatusCode() == 200) {
			String conResult = EntityUtils.toString(response.getEntity());
			JSONObject sobj = new JSONObject();
			
			sobj = JSONObject.parseObject(conResult);
			Integer code = Integer.valueOf(sobj.getString("errorCode"));
			if (code>0) {
				String msg = sobj.getString("errorInfo");
				
				return new HttpResult(Integer.valueOf(code),msg);
			} else {
				String ret = sobj.getString("errorInfo");	
				JSONObject sobj1 = new JSONObject();
				sobj1 = JSONObject.parseObject(sobj.getString("data"));	
				if(sobj1==null) {
					return null;
				}else {
					return new HttpResult(0,sobj1.toString());
				}
				
			}
		}else{
			String err = response.getStatusLine().getStatusCode() + "";
			return new HttpResult(1,err);
		}
    }
	/**
	 * 带参数的get方法
	 * 
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求参数
	 * @return
	 * @throws Exception
	 */
	public HttpResult doGetNew(String url, Map<String, Object> param, String va1, String va2) throws Exception {

		URIBuilder uriBuilder = new URIBuilder(url);
		if (param != null) {
			Set<Map.Entry<String, Object>> entrySet = param.entrySet();
			for (Map.Entry<String, Object> entry : entrySet) {
				uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
			}
		}

		// 创建httpGet
		HttpGet http = new HttpGet(uriBuilder.build());
		http.setHeader(va1, va2);
		CloseableHttpResponse httpResponse = null;
		try {
			// 执行
			httpResponse = httpClient.execute(http);
			// 处理结果
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String content = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
				return new HttpResult(HttpStatus.SC_OK, content);
			}
		} finally {
			if (httpResponse != null) {
				httpResponse.close();
			}
			httpClient.close();
		}
		return new HttpResult(httpResponse.getStatusLine().getStatusCode());
	}

	/**
	 * 带请求头和参数的put方法
	 * 
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求参数
	 * @return
	 * @throws Exception
	 */
	public HttpResult doPut(String url, Map<String, Object> param, String va1, String va2) throws Exception {

		// 创建httpPut
		HttpPut http = new HttpPut(url);
		http.setHeader(va1, va2);
		if (param != null) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			Set<Map.Entry<String, Object>> entrySet = param.entrySet();
			for (Map.Entry<String, Object> entry : entrySet) {
				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}

			// String text = JSON.toJSONString(param);
			// 设置参数
			// http.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
			String text = JSON.toJSONString(param);
			System.out.println(text);
			http.setEntity(new StringEntity(text, "utf-8"));
			http.setHeader("content-Type", "application/json;charset=UTF-8");
		}

		CloseableHttpResponse httpResponse = null;
		try {
			// 执行
			httpResponse = httpClient.execute(http);
			// 处理结果
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String content = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
				return new HttpResult(HttpStatus.SC_OK, content);
			}
		} finally {
			if (httpResponse != null) {
				httpResponse.close();
			}
			httpClient.close();
		}
		return new HttpResult(httpResponse.getStatusLine().getStatusCode());
	}

	/**
	 * Delete请求 带参数以及请求头
	 */
	public HttpResult doDelete(String url, Map<String, Object> param, String var1, String var2) throws Exception {
		if (param == null) {
			param = new HashMap<String, Object>();
		}
		param.put("_method", "delete");

		return doPostNew(url, null, var1, var2);
	}
	/**
	 * 发送http delete请求
	 */

}
