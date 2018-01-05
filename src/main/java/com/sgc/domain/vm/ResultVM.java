package com.sgc.domain.vm;

import java.io.Serializable;


public class ResultVM implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final Integer CODE = 0;

    private Integer code;//0成功 1失败

    private String msg="";//不能返回null

    private Object result;

    public ResultVM() {

    }

    public ResultVM(Integer code) {
        this.code = code;
    }

    public ResultVM(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVM(Integer code, Object result) {
        this.code = code;
        this.result = result;
    }

    public ResultVM(Object result) {
        this.result = result;
    }

    public static ResultVM error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static ResultVM error(String msg) {
        return error(500, msg);
    }

    public static ResultVM error(Integer code, String msg) {
        return new ResultVM(code, msg);
    }

    public static ResultVM ok(String msg) {
        return new ResultVM(CODE, msg);
    }

    public static ResultVM ok(Object result) {
        return new ResultVM(CODE, result);
    }

    public static ResultVM ok() {
        return new ResultVM(CODE);
    }


    public Integer getCode() {
        return code;
    }

    public ResultVM setCode(Integer code) {
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

    public ResultVM setResult(Object result) {
        this.result = result;
        return this;
    }
}
