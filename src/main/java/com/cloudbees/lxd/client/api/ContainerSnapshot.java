
package com.cloudbees.lxd.client.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    "architecture",
    "config",
    "created_at",
    "devices",
    "ephemeral",
    "expanded_config",
    "expanded_devices",
    "last_used_at",
    "name",
    "profiles",
    "stateful"
})
public class ContainerSnapshot implements Serializable
{

    /**
     * 
     * 
     */
    @JsonProperty("architecture")
    private java.lang.String architecture;
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
    @JsonProperty("created_at")
    private Date createdAt;
    /**
     * 
     * 
     */
    @JsonProperty("devices")
    private Map<String, Map<String, String>> devices;
    /**
     * 
     * 
     */
    @JsonProperty("ephemeral")
    private Boolean ephemeral;
    /**
     * 
     * 
     */
    @JsonProperty("expanded_config")
    private Map<String, String> expandedConfig;
    /**
     * 
     * 
     */
    @JsonProperty("expanded_devices")
    private Map<String, Map<String, String>> expandedDevices;
    /**
     * 
     * 
     */
    @JsonProperty("last_used_at")
    private Date lastUsedAt;
    /**
     * 
     * 
     */
    @JsonProperty("name")
    private java.lang.String name;
    /**
     * 
     * 
     */
    @JsonProperty("profiles")
    private List<java.lang.String> profiles = new ArrayList<java.lang.String>();
    /**
     * 
     * 
     */
    @JsonProperty("stateful")
    private Boolean stateful;
    @JsonIgnore
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ContainerSnapshot() {
    }

    /**
     * 
     * @param createdAt
     * @param expandedDevices
     * @param expandedConfig
     * @param devices
     * @param lastUsedAt
     * @param name
     * @param profiles
     * @param ephemeral
     * @param config
     * @param stateful
     * @param architecture
     */
    public ContainerSnapshot(java.lang.String architecture, Map<String, String> config, Date createdAt, Map<String, Map<String, String>> devices, Boolean ephemeral, Map<String, String> expandedConfig, Map<String, Map<String, String>> expandedDevices, Date lastUsedAt, java.lang.String name, List<java.lang.String> profiles, Boolean stateful) {
        this.architecture = architecture;
        this.config = config;
        this.createdAt = createdAt;
        this.devices = devices;
        this.ephemeral = ephemeral;
        this.expandedConfig = expandedConfig;
        this.expandedDevices = expandedDevices;
        this.lastUsedAt = lastUsedAt;
        this.name = name;
        this.profiles = profiles;
        this.stateful = stateful;
    }

    /**
     * 
     * 
     * @return
     *     The architecture
     */
    @JsonProperty("architecture")
    public java.lang.String getArchitecture() {
        return architecture;
    }

    /**
     * 
     * 
     * @param architecture
     *     The architecture
     */
    @JsonProperty("architecture")
    public void setArchitecture(java.lang.String architecture) {
        this.architecture = architecture;
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
     *     The createdAt
     */
    @JsonProperty("created_at")
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * 
     * @param createdAt
     *     The created_at
     */
    @JsonProperty("created_at")
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    /**
     * 
     * 
     * @return
     *     The ephemeral
     */
    @JsonProperty("ephemeral")
    public Boolean getEphemeral() {
        return ephemeral;
    }

    /**
     * 
     * 
     * @param ephemeral
     *     The ephemeral
     */
    @JsonProperty("ephemeral")
    public void setEphemeral(Boolean ephemeral) {
        this.ephemeral = ephemeral;
    }

    /**
     * 
     * 
     * @return
     *     The expandedConfig
     */
    @JsonProperty("expanded_config")
    public Map<String, String> getExpandedConfig() {
        return expandedConfig;
    }

    /**
     * 
     * 
     * @param expandedConfig
     *     The expanded_config
     */
    @JsonProperty("expanded_config")
    public void setExpandedConfig(Map<String, String> expandedConfig) {
        this.expandedConfig = expandedConfig;
    }

    /**
     * 
     * 
     * @return
     *     The expandedDevices
     */
    @JsonProperty("expanded_devices")
    public Map<String, Map<String, String>> getExpandedDevices() {
        return expandedDevices;
    }

    /**
     * 
     * 
     * @param expandedDevices
     *     The expanded_devices
     */
    @JsonProperty("expanded_devices")
    public void setExpandedDevices(Map<String, Map<String, String>> expandedDevices) {
        this.expandedDevices = expandedDevices;
    }

    /**
     * 
     * 
     * @return
     *     The lastUsedAt
     */
    @JsonProperty("last_used_at")
    public Date getLastUsedAt() {
        return lastUsedAt;
    }

    /**
     * 
     * 
     * @param lastUsedAt
     *     The last_used_at
     */
    @JsonProperty("last_used_at")
    public void setLastUsedAt(Date lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    /**
     * 
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public java.lang.String getName() {
        return name;
    }

    /**
     * 
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * 
     * 
     * @return
     *     The profiles
     */
    @JsonProperty("profiles")
    public List<java.lang.String> getProfiles() {
        return profiles;
    }

    /**
     * 
     * 
     * @param profiles
     *     The profiles
     */
    @JsonProperty("profiles")
    public void setProfiles(List<java.lang.String> profiles) {
        this.profiles = profiles;
    }

    /**
     * 
     * 
     * @return
     *     The stateful
     */
    @JsonProperty("stateful")
    public Boolean getStateful() {
        return stateful;
    }

    /**
     * 
     * 
     * @param stateful
     *     The stateful
     */
    @JsonProperty("stateful")
    public void setStateful(Boolean stateful) {
        this.stateful = stateful;
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
