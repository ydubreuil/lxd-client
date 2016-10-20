package com.cloudbees.lxd.client;

public class LxdClientException extends RuntimeException {
    public LxdClientException(Throwable throwable) {
        super(throwable);
    }

    public LxdClientException(String message) {
        super(message);
    }

    public LxdClientException(String message, Throwable t) {
        super(message, t);
    }

}
