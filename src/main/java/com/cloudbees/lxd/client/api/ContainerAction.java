package com.cloudbees.lxd.client.api;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ContainerAction {
    Stop("stop"),
    Start("start"),
    Restart("restart"),
    Freeze("freeze"),
    Unfreeze("unfreeze");

    private final String value;
    ContainerAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static Map<String, ContainerAction> FORMAT_MAP = Stream.of(ContainerAction.values()).collect(
        Collectors.toMap(s -> s.value, Function.identity()));

    @JsonCreator
    public static ContainerAction fromString(String action) {
        ContainerAction status = FORMAT_MAP.get(action);
        if (status == null) {
            throw new IllegalArgumentException(action + " has no corresponding value");
        }
        return status;
    }
}
