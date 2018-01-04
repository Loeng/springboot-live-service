package com.sgc.comm.filter;

public class ResultMsg {  
    private int code;  
    private String msg;  
    private Object p2pdata;  
      
    public ResultMsg(int Code, String Msg, Object P2pData)  
    {  
        this.code = Code;  
        this.msg = Msg;  
        this.p2pdata = P2pData;  
    }  
    public int getCode() {  
        return code;  
    }  
    public void setCode(int Code) {  
        this.code = Code;  
    }  
    public String getMsg() {  
        return msg;  
    }  
    public void setMsg(String Msg) {  
        this.msg = Msg;  
    }  
    public Object getP2pdata() {  
        return p2pdata;  
    }  
    public void setP2pdata(Object p2pdata) {  
        this.p2pdata = p2pdata;  
    }  
}
