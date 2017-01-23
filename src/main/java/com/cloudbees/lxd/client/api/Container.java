
package com.cloudbees.lxd.client.api;

import java.util.Date;
import java.util.Map;
import javax.annotation.Generated;
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
    "created_at",
    "expanded_config",
    "expanded_devices",
    "last_used_at",
    "name",
    "stateful",
    "status",
    "status_code"
})
public class Container extends ContainerPut
{

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
    @JsonProperty("stateful")
    private Boolean stateful;
    /**
     *
     *
     */
    @JsonProperty("status")
    private java.lang.String status;
    /**
     *
     *
     */
    @JsonProperty("status_code")
    private StatusCode statusCode;

    /**
     * No args constructor for use in serialization
     *
     */
    public Container() {
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

    /**
     *
     *
     * @return
     *     The status
     */
    @JsonProperty("status")
    public java.lang.String getStatus() {
        return status;
    }

    /**
     *
     *
     * @param status
     *     The status
     */
    @JsonProperty("status")
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /**
     *
     *
     * @return
     *     The statusCode
     */
    @JsonProperty("status_code")
    public StatusCode getStatusCode() {
        return statusCode;
    }

    /**
     *
     *
     * @param statusCode
     *     The status_code
     */
    @JsonProperty("status_code")
    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
