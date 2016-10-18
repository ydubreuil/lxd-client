package com.cloudbees.lxd.client.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LXDResponse<E> {
    @JsonProperty("type")
    private ResponseType type;

    /* Valid only for Sync responses */
    @JsonProperty("status")
    private String status;

    @JsonProperty("status_code")
    private Integer statusCode ;

    /* Valid only for Async responses */
    @JsonProperty("operation")
    private String operation;

    /* Valid only for Error responses */
    @JsonProperty("error_code")
    private Integer errorCode;

    @JsonProperty("error")
    private String error;

    @JsonProperty("metadata")
    private E data;

    @Override
    public String toString() {
        return "LXDResponse{" +
            "type=" + type +
            ", status='" + status + '\'' +
            ", statusCode=" + statusCode +
            ", operation='" + operation + '\'' +
            ", errorCode=" + errorCode +
            ", error='" + error + '\'' +
            ", data=" + data +
            '}';
    }
}
