package com.cnh.rvcalculatorprocessor.dto;



import com.microsoft.azure.functions.HttpRequestMessage;

import java.util.Optional;

public class RequestDTO<T> {
    private T t;
    HttpRequestMessage<Optional<T>> requestMessage;

    public RequestDTO(HttpRequestMessage<Optional<T>> requestMessage) {
        this.requestMessage = requestMessage;
    }

    public HttpRequestMessage<Optional<T>> getRequestMessage() {
        return requestMessage;
    }
}
