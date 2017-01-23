
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
    "address",
    "family",
    "netmask",
    "scope"
})
public class ContainerStateNetworkAddress implements Serializable
{

    /**
     * 
     * 
     */
    @JsonProperty("address")
    private String address;
    /**
     * 
     * 
     */
    @JsonProperty("family")
    private String family;
    /**
     * 
     * 
     */
    @JsonProperty("netmask")
    private String netmask;
    /**
     * 
     * 
     */
    @JsonProperty("scope")
    private String scope;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ContainerStateNetworkAddress() {
    }

    /**
     * 
     * @param address
     * @param netmask
     * @param scope
     * @param family
     */
    public ContainerStateNetworkAddress(String address, String family, String netmask, String scope) {
        this.address = address;
        this.family = family;
        this.netmask = netmask;
        this.scope = scope;
    }

    /**
     * 
     * 
     * @return
     *     The address
     */
    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    /**
     * 
     * 
     * @param address
     *     The address
     */
    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 
     * 
     * @return
     *     The family
     */
    @JsonProperty("family")
    public String getFamily() {
        return family;
    }

    /**
     * 
     * 
     * @param family
     *     The family
     */
    @JsonProperty("family")
    public void setFamily(String family) {
        this.family = family;
    }

    /**
     * 
     * 
     * @return
     *     The netmask
     */
    @JsonProperty("netmask")
    public String getNetmask() {
        return netmask;
    }

    /**
     * 
     * 
     * @param netmask
     *     The netmask
     */
    @JsonProperty("netmask")
    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    /**
     * 
     * 
     * @return
     *     The scope
     */
    @JsonProperty("scope")
    public String getScope() {
        return scope;
    }

    /**
     * 
     * 
     * @param scope
     *     The scope
     */
    @JsonProperty("scope")
    public void setScope(String scope) {
        this.scope = scope;
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
