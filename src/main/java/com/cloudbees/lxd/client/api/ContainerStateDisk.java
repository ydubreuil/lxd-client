
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
    "usage"
})
public class ContainerStateDisk implements Serializable
{

    /**
     * 
     * 
     */
    @JsonProperty("usage")
    private Long usage;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ContainerStateDisk() {
    }

    /**
     * 
     * @param usage
     */
    public ContainerStateDisk(Long usage) {
        this.usage = usage;
    }

    /**
     * 
     * 
     * @return
     *     The usage
     */
    @JsonProperty("usage")
    public Long getUsage() {
        return usage;
    }

    /**
     * 
     * 
     * @param usage
     *     The usage
     */
    @JsonProperty("usage")
    public void setUsage(Long usage) {
        this.usage = usage;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
