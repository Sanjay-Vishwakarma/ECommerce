package com.sj.ecommerce.reponse;

public class Response<T> {

    private T data;
    private String code;
    private String message;
    private String status;



    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setStatus(String success) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}

