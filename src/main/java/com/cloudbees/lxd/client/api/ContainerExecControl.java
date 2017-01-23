
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
    "args",
    "command",
    "signal"
})
public class ContainerExecControl implements Serializable
{

    /**
     * 
     * 
     */
    @JsonProperty("args")
    private Map<String, String> args;
    /**
     * 
     * 
     */
    @JsonProperty("command")
    private java.lang.String command;
    /**
     * 
     * 
     */
    @JsonProperty("signal")
    private Integer signal;
    @JsonIgnore
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ContainerExecControl() {
    }

    /**
     * 
     * @param args
     * @param signal
     * @param command
     */
    public ContainerExecControl(Map<String, String> args, java.lang.String command, Integer signal) {
        this.args = args;
        this.command = command;
        this.signal = signal;
    }

    /**
     * 
     * 
     * @return
     *     The args
     */
    @JsonProperty("args")
    public Map<String, String> getArgs() {
        return args;
    }

    /**
     * 
     * 
     * @param args
     *     The args
     */
    @JsonProperty("args")
    public void setArgs(Map<String, String> args) {
        this.args = args;
    }

    /**
     * 
     * 
     * @return
     *     The command
     */
    @JsonProperty("command")
    public java.lang.String getCommand() {
        return command;
    }

    /**
     * 
     * 
     * @param command
     *     The command
     */
    @JsonProperty("command")
    public void setCommand(java.lang.String command) {
        this.command = command;
    }

    /**
     * 
     * 
     * @return
     *     The signal
     */
    @JsonProperty("signal")
    public Integer getSignal() {
        return signal;
    }

    /**
     * 
     * 
     * @param signal
     *     The signal
     */
    @JsonProperty("signal")
    public void setSignal(Integer signal) {
        this.signal = signal;
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
