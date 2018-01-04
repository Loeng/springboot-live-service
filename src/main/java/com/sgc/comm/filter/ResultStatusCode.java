package com.sgc.comm.filter;

public enum ResultStatusCode {
	OK(0, "OK"),  
	INVALID_CLIENTID(30003, "无效的 clientid"),  
	INVALID_PASSWORD(30004, "用户名或密码错误"),  
	INVALID_CAPTCHA(30005, "无效的 验证码 or 验证码 过期"),  
	INVALID_TOKEN(30006, "无效的 token"),
    SYSTEM_ERR(30001, "系统错误");  
      
    private int code;  
    private String msg;  
    public int getCode() {  
        return code;  
    }  
  
    public void setErrcode(int code) {  
        this.code = code;  
    }  
  
    public String getmsg() {  
        return msg;  
    }  
  
    public void setmsg(String msg) {  
        this.msg = msg;  
    }  
    private ResultStatusCode(int Code, String Msg)  
    {  
        this.code = Code;  
        this.msg = Msg;  
    }  
}
