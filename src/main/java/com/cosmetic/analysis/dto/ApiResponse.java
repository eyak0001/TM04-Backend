package com.cosmetic.analysis.dto;


public class ApiResponse<T> {
    private int code;
    private T data;
    private String msg;
    private long total;

    public ApiResponse() {}

    public ApiResponse(int code, T data, String msg, long total) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.total = total;
    }

    // Getters & Setters
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
}
