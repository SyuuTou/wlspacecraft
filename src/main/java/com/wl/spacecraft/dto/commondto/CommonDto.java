package com.wl.spacecraft.dto.commondto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CommonDto<T> {

    private String message;
    private Integer status ;
    /**
     * 冗余字段，前端接收时区分响应体类型用
     */
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;
    private T data;

    public CommonDto(){

    }

    public CommonDto(String message, Integer status, String type, T data) {
        this.message = message;
        this.status = status;
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommonDto{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", type='" + type + '\'' +
                ", data=" + data +
                '}';
    }
}

