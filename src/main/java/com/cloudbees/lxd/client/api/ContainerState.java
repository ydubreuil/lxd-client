
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
    "cpu",
    "disk",
    "memory",
    "network",
    "pid",
    "processes",
    "status",
    "status_code"
})
public class ContainerState implements Serializable
{

    /**
     * 
     * 
     */
    @JsonProperty("cpu")
    private ContainerStateCPU cpu;
    /**
     * 
     * 
     */
    @JsonProperty("disk")
    private Map<String, ContainerStateDisk> disk;
    /**
     * 
     * 
     */
    @JsonProperty("memory")
    private ContainerStateMemory memory;
    /**
     * 
     * 
     */
    @JsonProperty("network")
    private Map<String, ContainerStateNetwork> network;
    /**
     * 
     * 
     */
    @JsonProperty("pid")
    private Long pid;
    /**
     * 
     * 
     */
    @JsonProperty("processes")
    private Long processes;
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
    @JsonIgnore
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ContainerState() {
    }

    /**
     * 
     * @param disk
     * @param processes
     * @param memory
     * @param cpu
     * @param pid
     * @param network
     * @param status
     * @param statusCode
     */
    public ContainerState(ContainerStateCPU cpu, Map<String, ContainerStateDisk> disk, ContainerStateMemory memory, Map<String, ContainerStateNetwork> network, Long pid, Long processes, java.lang.String status, StatusCode statusCode) {
        this.cpu = cpu;
        this.disk = disk;
        this.memory = memory;
        this.network = network;
        this.pid = pid;
        this.processes = processes;
        this.status = status;
        this.statusCode = statusCode;
    }

    /**
     * 
     * 
     * @return
     *     The cpu
     */
    @JsonProperty("cpu")
    public ContainerStateCPU getCpu() {
        return cpu;
    }

    /**
     * 
     * 
     * @param cpu
     *     The cpu
     */
    @JsonProperty("cpu")
    public void setCpu(ContainerStateCPU cpu) {
        this.cpu = cpu;
    }

    /**
     * 
     * 
     * @return
     *     The disk
     */
    @JsonProperty("disk")
    public Map<String, ContainerStateDisk> getDisk() {
        return disk;
    }

    /**
     * 
     * 
     * @param disk
     *     The disk
     */
    @JsonProperty("disk")
    public void setDisk(Map<String, ContainerStateDisk> disk) {
        this.disk = disk;
    }

    /**
     * 
     * 
     * @return
     *     The memory
     */
    @JsonProperty("memory")
    public ContainerStateMemory getMemory() {
        return memory;
    }

    /**
     * 
     * 
     * @param memory
     *     The memory
     */
    @JsonProperty("memory")
    public void setMemory(ContainerStateMemory memory) {
        this.memory = memory;
    }

    /**
     * 
     * 
     * @return
     *     The network
     */
    @JsonProperty("network")
    public Map<String, ContainerStateNetwork> getNetwork() {
        return network;
    }

    /**
     * 
     * 
     * @param network
     *     The network
     */
    @JsonProperty("network")
    public void setNetwork(Map<String, ContainerStateNetwork> network) {
        this.network = network;
    }

    /**
     * 
     * 
     * @return
     *     The pid
     */
    @JsonProperty("pid")
    public Long getPid() {
        return pid;
    }

    /**
     * 
     * 
     * @param pid
     *     The pid
     */
    @JsonProperty("pid")
    public void setPid(Long pid) {
        this.pid = pid;
    }

    /**
     * 
     * 
     * @return
     *     The processes
     */
    @JsonProperty("processes")
    public Long getProcesses() {
        return processes;
    }

    /**
     * 
     * 
     * @param processes
     *     The processes
     */
    @JsonProperty("processes")
    public void setProcesses(Long processes) {
        this.processes = processes;
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

    @JsonAnyGetter
    public Map<java.lang.String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(java.lang.String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
