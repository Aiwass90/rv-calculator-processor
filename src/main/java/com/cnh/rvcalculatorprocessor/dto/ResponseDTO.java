package com.cnh.rvcalculatorprocessor.dto;

import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatusType;

import java.util.Map;

public class ResponseDTO implements HttpResponseMessage {
    HttpStatusType status;
    Map<String,String> headers;
    Object body;

    @Override
    public HttpStatusType getStatus() {
        return status;
    }

    @Override
    public String getHeader(String key) {
        return headers.get(key);
    }

    @Override
    public Object getBody() {
        return body;
    }

    protected void setStatus(HttpStatusType status) {
        this.status = status;
    }


    protected void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    protected void setBody(Object body) {
        this.body = body;
    }
}
