
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
    "swap_usage",
    "swap_usage_peak",
    "usage",
    "usage_peak"
})
public class ContainerStateMemory implements Serializable
{

    /**
     * 
     * 
     */
    @JsonProperty("swap_usage")
    private Long swapUsage;
    /**
     * 
     * 
     */
    @JsonProperty("swap_usage_peak")
    private Long swapUsagePeak;
    /**
     * 
     * 
     */
    @JsonProperty("usage")
    private Long usage;
    /**
     * 
     * 
     */
    @JsonProperty("usage_peak")
    private Long usagePeak;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ContainerStateMemory() {
    }

    /**
     * 
     * @param swapUsagePeak
     * @param usagePeak
     * @param swapUsage
     * @param usage
     */
    public ContainerStateMemory(Long swapUsage, Long swapUsagePeak, Long usage, Long usagePeak) {
        this.swapUsage = swapUsage;
        this.swapUsagePeak = swapUsagePeak;
        this.usage = usage;
        this.usagePeak = usagePeak;
    }

    /**
     * 
     * 
     * @return
     *     The swapUsage
     */
    @JsonProperty("swap_usage")
    public Long getSwapUsage() {
        return swapUsage;
    }

    /**
     * 
     * 
     * @param swapUsage
     *     The swap_usage
     */
    @JsonProperty("swap_usage")
    public void setSwapUsage(Long swapUsage) {
        this.swapUsage = swapUsage;
    }

    /**
     * 
     * 
     * @return
     *     The swapUsagePeak
     */
    @JsonProperty("swap_usage_peak")
    public Long getSwapUsagePeak() {
        return swapUsagePeak;
    }

    /**
     * 
     * 
     * @param swapUsagePeak
     *     The swap_usage_peak
     */
    @JsonProperty("swap_usage_peak")
    public void setSwapUsagePeak(Long swapUsagePeak) {
        this.swapUsagePeak = swapUsagePeak;
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

    /**
     * 
     * 
     * @return
     *     The usagePeak
     */
    @JsonProperty("usage_peak")
    public Long getUsagePeak() {
        return usagePeak;
    }

    /**
     * 
     * 
     * @param usagePeak
     *     The usage_peak
     */
    @JsonProperty("usage_peak")
    public void setUsagePeak(Long usagePeak) {
        this.usagePeak = usagePeak;
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
