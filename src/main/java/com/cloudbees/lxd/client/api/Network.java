
package com.cloudbees.lxd.client.api;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "config",
    "managed",
    "name",
    "type",
    "used_by"
})
public class Network implements Serializable {

    @JsonProperty("config")
    private Map<String, String> config;

    @JsonProperty("managed")
    private Boolean managed;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;

    @JsonProperty("used_by")
    private List<String> usedBy = new ArrayList<String>();

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Network() {
    }

    /**
     *
     * @param config
     * @param managed
     * @param name
     * @param type
     * @param usedBy
     */
    public Network(Map<String, String> config, Boolean managed, String name, String type, List<String> usedBy) {
        this.config = config;
        this.managed = managed;
        this.name = name;
        this.type = type;
        this.usedBy = usedBy;
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
     *     The managed
     */
    @JsonProperty("managed")
    public Boolean getManaged() {
        return managed;
    }

    /**
     *
     *
     * @param managed
     *     The managed
     */
    @JsonProperty("managed")
    public void setManaged(Boolean managed) {
        this.managed = managed;
    }

    /**
     *
     *
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     *
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     *
     * @return
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     *
     *
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     *
     * @return
     *     The usedBy
     */
    @JsonProperty("used_by")
    public List<String> getUsedBy() {
        return usedBy;
    }

    /**
     *
     *
     * @param usedBy
     *     The used_by
     */
    @JsonProperty("used_by")
    public void setUsedBy(List<String> usedBy) {
        this.usedBy = usedBy;
    }


    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "Network{" +
            "config=" + config +
            ", managed=" + managed +
            ", name='" + name + '\'' +
            ", type='" + type + '\'' +
            ", usedBy=" + usedBy +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
