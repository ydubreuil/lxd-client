
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
public class ServerPut implements Serializable
{

    /**
     * 
     * 
     */
    @JsonProperty("config")
    private Map<String, Object> config;
    @JsonIgnore
    private Map<java.lang.String, java.lang.Object> additionalProperties = new HashMap<java.lang.String, java.lang.Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ServerPut() {
    }

    /**
     * 
     * @param config
     */
    public ServerPut(Map<String, Object> config) {
        this.config = config;
    }

    /**
     * 
     * 
     * @return
     *     The config
     */
    @JsonProperty("config")
    public Map<String, Object> getConfig() {
        return config;
    }

    /**
     * 
     * 
     * @param config
     *     The config
     */
    @JsonProperty("config")
    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    @JsonAnyGetter
    public Map<java.lang.String, java.lang.Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(java.lang.String name, java.lang.Object value) {
        this.additionalProperties.put(name, value);
    }

}
