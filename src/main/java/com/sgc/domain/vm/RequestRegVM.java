package com.sgc.domain.vm;

import lombok.Data;

import java.io.Serializable;

@Data
public class RequestRegVM implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final Integer CODE = 0;

    private Integer code;//0成功 失败

    private String msg="";//不能返回null

    private Object result;

    public RequestRegVM() {

    }

    public RequestRegVM(Integer code) {
        this.code = code;
    }

    public RequestRegVM(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public RequestRegVM(Integer code, Object result) {
        this.code = code;
        this.result = result;
    }

    public RequestRegVM(Object result) {
        this.result = result;
    }

    public static RequestRegVM error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static RequestRegVM error(String msg) {
        return error(500, msg);
    }

    public static RequestRegVM error(Integer code, String msg) {
        return new RequestRegVM(code, msg);
    }

    public static RequestRegVM ok(String msg) {
        return new RequestRegVM(CODE, msg);
    }

    public static RequestRegVM ok(Object result) {
        return new RequestRegVM(CODE, result);
    }

    public static RequestRegVM ok() {
        return new RequestRegVM(CODE);
    }


    public Integer getCode() {
        return code;
    }

    public RequestRegVM setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public RequestRegVM setResult(Object result) {
        this.result = result;
        return this;
    }
}
