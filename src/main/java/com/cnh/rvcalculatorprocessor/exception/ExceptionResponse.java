package com.cnh.rvcalculatorprocessor.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.microsoft.azure.functions.HttpStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

@Getter
@Setter
public class ExceptionResponse {
    // customizing timestamp serialization format
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;

    private int code;

    private String status;

    private String message;

    private String stackTrace;

    private Object data;

    public ExceptionResponse() {
        timestamp = new Date();
    }

    public ExceptionResponse(HttpStatus httpStatus, String message) {
        this();
        this.code = httpStatus.value();
        this.status = httpStatus.name();
        this.message = message;
    }

    public ExceptionResponse(HttpStatus httpStatus, String message, String stackTrace) {
        this(httpStatus, message);
        this.stackTrace = stackTrace;
    }

    public ExceptionResponse(HttpStatus httpStatus, Exception e) {
        this(httpStatus, e.getMessage());
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        this.stackTrace = stringWriter.toString();
    }

    public ExceptionResponse(HttpStatus httpStatus, String message, String stackTrace, Object data) {
        this(httpStatus, message, stackTrace);

        this.data = data;
    }
}
