package com.cloudbees.lxd.client.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
})
public final class ProfilePost extends ProfilePut {
    @JsonProperty("name")
    private String name;

    public ProfilePost(String name, ProfilePut profilePut) {
        super(profilePut);
        this.name = name;
    }
}
