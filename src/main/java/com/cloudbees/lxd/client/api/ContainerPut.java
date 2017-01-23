
package com.cloudbees.lxd.client.api;

import java.io.Serializable;
import java.util.ArrayList;
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
    "devices",
    "ephemeral",
    "profiles",
    "restore"
})
public class ContainerPut implements Serializable
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
    @JsonProperty("profiles")
    private List<java.lang.String> profiles = new ArrayList<java.lang.String>();
    /**
     *
     *
     */
    @JsonProperty("restore")
    private java.lang.String restore;

    /**
     * No args constructor for use in serialization
     *
     */
    public ContainerPut() {
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
     *     The restore
     */
    @JsonProperty("restore")
    public java.lang.String getRestore() {
        return restore;
    }

    /**
     *
     *
     * @param restore
     *     The restore
     */
    @JsonProperty("restore")
    public void setRestore(java.lang.String restore) {
        this.restore = restore;
    }
}
