package com.wl.spacecraft.exception;

import java.io.Serializable;

public class InvoiceException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -5442167302893125029L;

    private String code;
    private String msg;
    private Throwable e;
    private Object data;

    public InvoiceException(InvoiceExceptEnum exceptEnum){
        this.code = exceptEnum.getCode();
        this.msg = exceptEnum.getName();
    }

    public InvoiceException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public InvoiceException(String code, String msg, Object data) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public InvoiceException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.e = e;
    }

    public InvoiceException(String code, String msg, Throwable e) {
        super(msg, e);
        this.code = code;
        this.msg = msg;
        this.e = e;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}
