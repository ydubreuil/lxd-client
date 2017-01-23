
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
    "addresses",
    "counters",
    "host_name",
    "hwaddr",
    "mtu",
    "state",
    "type"
})
public class ContainerStateNetwork implements Serializable
{

    /**
     * 
     * 
     */
    @JsonProperty("addresses")
    private List<ContainerStateNetworkAddress> addresses = new ArrayList<ContainerStateNetworkAddress>();
    /**
     * 
     * 
     */
    @JsonProperty("counters")
    private ContainerStateNetworkCounters counters;
    /**
     * 
     * 
     */
    @JsonProperty("host_name")
    private String hostName;
    /**
     * 
     * 
     */
    @JsonProperty("hwaddr")
    private String hwaddr;
    /**
     * 
     * 
     */
    @JsonProperty("mtu")
    private Integer mtu;
    /**
     * 
     * 
     */
    @JsonProperty("state")
    private String state;
    /**
     * 
     * 
     */
    @JsonProperty("type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ContainerStateNetwork() {
    }

    /**
     * 
     * @param hostName
     * @param addresses
     * @param hwaddr
     * @param counters
     * @param state
     * @param type
     * @param mtu
     */
    public ContainerStateNetwork(List<ContainerStateNetworkAddress> addresses, ContainerStateNetworkCounters counters, String hostName, String hwaddr, Integer mtu, String state, String type) {
        this.addresses = addresses;
        this.counters = counters;
        this.hostName = hostName;
        this.hwaddr = hwaddr;
        this.mtu = mtu;
        this.state = state;
        this.type = type;
    }

    /**
     * 
     * 
     * @return
     *     The addresses
     */
    @JsonProperty("addresses")
    public List<ContainerStateNetworkAddress> getAddresses() {
        return addresses;
    }

    /**
     * 
     * 
     * @param addresses
     *     The addresses
     */
    @JsonProperty("addresses")
    public void setAddresses(List<ContainerStateNetworkAddress> addresses) {
        this.addresses = addresses;
    }

    /**
     * 
     * 
     * @return
     *     The counters
     */
    @JsonProperty("counters")
    public ContainerStateNetworkCounters getCounters() {
        return counters;
    }

    /**
     * 
     * 
     * @param counters
     *     The counters
     */
    @JsonProperty("counters")
    public void setCounters(ContainerStateNetworkCounters counters) {
        this.counters = counters;
    }

    /**
     * 
     * 
     * @return
     *     The hostName
     */
    @JsonProperty("host_name")
    public String getHostName() {
        return hostName;
    }

    /**
     * 
     * 
     * @param hostName
     *     The host_name
     */
    @JsonProperty("host_name")
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * 
     * 
     * @return
     *     The hwaddr
     */
    @JsonProperty("hwaddr")
    public String getHwaddr() {
        return hwaddr;
    }

    /**
     * 
     * 
     * @param hwaddr
     *     The hwaddr
     */
    @JsonProperty("hwaddr")
    public void setHwaddr(String hwaddr) {
        this.hwaddr = hwaddr;
    }

    /**
     * 
     * 
     * @return
     *     The mtu
     */
    @JsonProperty("mtu")
    public Integer getMtu() {
        return mtu;
    }

    /**
     * 
     * 
     * @param mtu
     *     The mtu
     */
    @JsonProperty("mtu")
    public void setMtu(Integer mtu) {
        this.mtu = mtu;
    }

    /**
     * 
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public String getState() {
        return state;
    }

    /**
     * 
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
