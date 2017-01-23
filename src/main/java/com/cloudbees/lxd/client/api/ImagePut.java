
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
    "auto_update",
    "properties",
    "public"
})
public class ImagePut implements Serializable
{

    /**
     * 
     * 
     */
    @JsonProperty("auto_update")
    private Boolean autoUpdate;
    /**
     * 
     * 
     */
    @JsonProperty("properties")
    private Map<String, String> properties;
    /**
     * 
     * 
     */
    @JsonProperty("public")
    private Boolean _public;
    @JsonIgnore
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ImagePut() {
    }

    /**
     * 
     * @param autoUpdate
     * @param _public
     * @param properties
     */
    public ImagePut(Boolean autoUpdate, Map<String, String> properties, Boolean _public) {
        this.autoUpdate = autoUpdate;
        this.properties = properties;
        this._public = _public;
    }

    /**
     * 
     * 
     * @return
     *     The autoUpdate
     */
    @JsonProperty("auto_update")
    public Boolean getAutoUpdate() {
        return autoUpdate;
    }

    /**
     * 
     * 
     * @param autoUpdate
     *     The auto_update
     */
    @JsonProperty("auto_update")
    public void setAutoUpdate(Boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    /**
     * 
     * 
     * @return
     *     The properties
     */
    @JsonProperty("properties")
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * 
     * 
     * @param properties
     *     The properties
     */
    @JsonProperty("properties")
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    /**
     * 
     * 
     * @return
     *     The _public
     */
    @JsonProperty("public")
    public Boolean getPublic() {
        return _public;
    }

    /**
     * 
     * 
     * @param _public
     *     The public
     */
    @JsonProperty("public")
    public void setPublic(Boolean _public) {
        this._public = _public;
    }

    @JsonAnyGetter
    public Map<java.lang.String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(java.lang.String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
