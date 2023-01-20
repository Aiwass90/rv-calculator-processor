package com.cnh.rvcalculatorprocessor.dto;

import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.HttpStatusType;

import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder implements HttpResponseMessage.Builder {

    HttpStatusType statusType;
    Map<String, String> headers;
    Object body;

    @Override
    public HttpResponseMessage.Builder status(HttpStatusType httpStatusType) {
        this.statusType = httpStatusType;
        return this;
    }

    @Override
    public HttpResponseMessage.Builder header(String s, String s1) {
        if(headers == null) {
            headers = new HashMap<>();
        }

        this.headers.put(s, s1);
        return this;
    }

    @Override
    public HttpResponseMessage.Builder body(Object o) {
        this.body = o;
        return this;
    }

    @Override
    public HttpResponseMessage build() {
        ResponseDTO responseMessage = new ResponseDTO();

        if (this.statusType == null)
            this.statusType = HttpStatus.OK;
        responseMessage.setStatus(this.statusType);

        if (this.headers == null)
            this.headers = new HashMap<>();
        responseMessage.setHeaders(this.headers);

        if (body == null)
            this.body = "";
        responseMessage.setBody(this.body);
        return responseMessage;
    }
}
