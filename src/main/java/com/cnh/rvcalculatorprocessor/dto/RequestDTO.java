package com.cnh.rvcalculatorprocessor.dto;



import com.microsoft.azure.functions.HttpRequestMessage;

import java.util.Optional;

public class RequestDTO<T> {
    private T t;
    HttpRequestMessage<Optional<T>> requestMessage;
    RequestDTO<T> req;

    public RequestDTO(HttpRequestMessage<Optional<T>> requestMessage) {
        this.requestMessage = requestMessage;
    }

    public RequestDTO(RequestDTO<T> requestMessage) {
        this.req = requestMessage;
    }

    public RequestDTO(T message) {
        t = message;
    }

    public HttpRequestMessage<Optional<T>> getRequestMessage() {
        return requestMessage;
    }
}
