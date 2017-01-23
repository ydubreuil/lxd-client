
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
    "config"
})
public class NetworkPut implements Serializable
{

    /**
     *
     *
     */
    @JsonProperty("config")
    private Map<String, String> config;

    /**
     * No args constructor for use in serialization
     *
     */
    public NetworkPut() {
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
}
