package com.bah.comet.cometapi.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericHttpException extends Exception {
    private HttpStatus status;

    public GenericHttpException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public String getMessage() {
        return super.getMessage();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
