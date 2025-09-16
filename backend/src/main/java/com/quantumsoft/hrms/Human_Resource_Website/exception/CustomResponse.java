package com.quantumsoft.hrms.Human_Resource_Website.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class CustomResponse {
    private LocalDate localDate;
    private LocalTime localTime;
    private Integer httpStatusCode;
    private HttpStatus httpStatus;
    private String customMessage;
    private String apiPath;

    public CustomResponse(LocalDate localDate, LocalTime localTime, Integer httpStatusCode, HttpStatus httpStatus, String customMessage, String apiPath) {
        this.localDate = localDate;
        this.localTime = localTime;
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.customMessage = customMessage;
        this.apiPath = apiPath;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }
}
