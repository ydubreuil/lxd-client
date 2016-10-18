package com.cloudbees.lxd.client.api;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResponseType {

    SYNC("sync"), ASYNC("async"), ERROR("error");

    private String value;
    ResponseType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
