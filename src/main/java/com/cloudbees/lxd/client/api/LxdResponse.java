package com.cloudbees.lxd.client.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LxdResponse<E> {
    @JsonProperty("type")
    private ResponseType type;

    /* Valid only for Sync responses */
    @JsonProperty("status")
    private String status;

    @JsonProperty("status_code")
    private Integer statusCode ;

    /* Valid only for Async responses */
    @JsonProperty("operation")
    private String operationUrl;

    /* Valid only for Error responses */
    @JsonProperty("error_code")
    private Integer errorCode;

    @JsonProperty("error")
    private String error;

    @JsonProperty("metadata")
    private E data;

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getOperationUrl() {
        return operationUrl;
    }

    public void setOperationUrl(String operationUrl) {
        this.operationUrl = operationUrl;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LXDResponse{" +
            "type=" + type +
            ", status='" + status + '\'' +
            ", statusCode=" + statusCode +
            ", operationUrl='" + operationUrl + '\'' +
            ", errorCode=" + errorCode +
            ", error='" + error + '\'' +
            ", data=" + data +
            '}';
    }
}
