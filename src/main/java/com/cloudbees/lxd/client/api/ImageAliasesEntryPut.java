
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
    "description",
    "target"
})
public class ImageAliasesEntryPut implements Serializable
{

    /**
     * 
     * 
     */
    @JsonProperty("description")
    private String description;
    /**
     * 
     * 
     */
    @JsonProperty("target")
    private String target;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ImageAliasesEntryPut() {
    }

    /**
     * 
     * @param description
     * @param target
     */
    public ImageAliasesEntryPut(String description, String target) {
        this.description = description;
        this.target = target;
    }

    /**
     * 
     * 
     * @return
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * 
     * 
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * 
     * @return
     *     The target
     */
    @JsonProperty("target")
    public String getTarget() {
        return target;
    }

    /**
     * 
     * 
     * @param target
     *     The target
     */
    @JsonProperty("target")
    public void setTarget(String target) {
        this.target = target;
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
