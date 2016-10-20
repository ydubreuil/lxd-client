package com.cloudbees.lxd.client.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "class",
    "created_at",
    "updated_at",
    "status",
    "status_code",
    "resources",
    "metadata",
    "may_cancel",
    "err"
})
public class AsyncOperation implements Serializable {
    @JsonProperty("id")
    private String id;

    @JsonProperty("class")
    private String clazz;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("status")
    private String status;

    @JsonProperty("status_code")
    private Status statusCode;

    @JsonProperty("resources")
    private Map<String, List<String>> resources;

    @JsonProperty("metadata")
    private String metadata;

    @JsonProperty("may_cancel")
    private String mayCancel;

    @JsonProperty("err")
    private String err;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static enum Status {
        OperationCreated(100),
        Started(101),
        Stopped(102),
        Running(103),
        Cancelling(104),
        Pending(105),
        Starting(106),
        Stopping(107),
        Aborting(108),
        Freezing(109),
        Frozen(110),
        Thawed(111),
        Error(112),
        Success(200),
        Failure(400),
        Cancelled(401);

        private final int value;
        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        private static Map<Integer, Status> FORMAT_MAP = Stream.of(Status.values()).collect(
            Collectors.toMap(s -> s.value, Function.identity()));

        @JsonCreator
        public static Status fromString(int code) {
            Status status = FORMAT_MAP.get(code);
            if (status == null) {
                throw new IllegalArgumentException(code + " has no corresponding value");
            }
            return status;
        }
    }
}
