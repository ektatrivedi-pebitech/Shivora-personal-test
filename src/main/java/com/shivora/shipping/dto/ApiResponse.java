
package com.shivora.shipping.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ApiResponse<T> {

    private boolean success;
    private int status;
    private String code;
    private String message;
    private T data;
    private List<String> errors;
    private Instant timestamp;
    private String traceId;

    public ApiResponse() {
        this.timestamp = Instant.now();
        this.traceId = UUID.randomUUID().toString();
    }

    public static <T> ApiResponse<T> success(
            int status, String message, T data) {

        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setStatus(status);
        response.setCode("SUCCESS");
        response.setMessage(message);
        response.setData(data);
        response.setErrors(List.of());

        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
