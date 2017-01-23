
package com.cloudbees.lxd.client.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "config",
    "description",
    "devices"
})
public class ProfilePut implements Serializable
{

    /**
     *
     *
     */
    @JsonProperty("config")
    private Map<String, String> config;
    /**
     *
     *
     */
    @JsonProperty("description")
    private java.lang.String description;
    /**
     *
     *
     */
    @JsonProperty("devices")
    private Map<String, Map<String, String>> devices;

    /**
     * No args constructor for use in serialization
     *
     */
    public ProfilePut() {
    }

    /**
     *
     *
     * @return
     *     The config
     */
    @JsonProperty("config")
    public Map<String, String> getConfig() {
        return config;
    }

    /**
     *
     *
     * @param config
     *     The config
     */
    @JsonProperty("config")
    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    /**
     *
     *
     * @return
     *     The description
     */
    @JsonProperty("description")
    public java.lang.String getDescription() {
        return description;
    }

    /**
     *
     *
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    /**
     *
     *
     * @return
     *     The devices
     */
    @JsonProperty("devices")
    public Map<String, Map<String, String>> getDevices() {
        return devices;
    }

    /**
     *
     *
     * @param devices
     *     The devices
     */
    @JsonProperty("devices")
    public void setDevices(Map<String, Map<String, String>> devices) {
        this.devices = devices;
    }
}
