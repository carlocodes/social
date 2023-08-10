package com.carlocodes.scoial.exceptions;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

public class ValidationErrorResponse {
    private OffsetDateTime timestamp;
    private int status;
    private String error;
    private String path;
    private Map<String, String> fieldErrors = new HashMap<>();

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public void addFieldError(String fieldName, String errorMessage) {
        fieldErrors.put(fieldName, errorMessage);
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
