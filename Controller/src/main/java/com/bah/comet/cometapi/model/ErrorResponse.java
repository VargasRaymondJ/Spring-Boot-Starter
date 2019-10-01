package com.bah.comet.cometapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {
    private String message;
    private String status;
    private String httpReason;
    private int httpCode;

    public ErrorResponse(String message, String status, String httpReason, int httpCode) {
        super();
        this.message = message;
        this.status = status;
        this.httpReason = httpReason;
        this.httpCode = httpCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHttpReason() {
        return httpReason;
    }

    public void setHttpReason(String httpReason) {
        this.httpReason = httpReason;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }
}
