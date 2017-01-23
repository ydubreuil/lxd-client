
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
    "bytes_received",
    "bytes_sent",
    "packets_received",
    "packets_sent"
})
public class ContainerStateNetworkCounters implements Serializable
{

    /**
     * 
     * 
     */
    @JsonProperty("bytes_received")
    private Long bytesReceived;
    /**
     * 
     * 
     */
    @JsonProperty("bytes_sent")
    private Long bytesSent;
    /**
     * 
     * 
     */
    @JsonProperty("packets_received")
    private Long packetsReceived;
    /**
     * 
     * 
     */
    @JsonProperty("packets_sent")
    private Long packetsSent;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ContainerStateNetworkCounters() {
    }

    /**
     * 
     * @param bytesReceived
     * @param packetsReceived
     * @param packetsSent
     * @param bytesSent
     */
    public ContainerStateNetworkCounters(Long bytesReceived, Long bytesSent, Long packetsReceived, Long packetsSent) {
        this.bytesReceived = bytesReceived;
        this.bytesSent = bytesSent;
        this.packetsReceived = packetsReceived;
        this.packetsSent = packetsSent;
    }

    /**
     * 
     * 
     * @return
     *     The bytesReceived
     */
    @JsonProperty("bytes_received")
    public Long getBytesReceived() {
        return bytesReceived;
    }

    /**
     * 
     * 
     * @param bytesReceived
     *     The bytes_received
     */
    @JsonProperty("bytes_received")
    public void setBytesReceived(Long bytesReceived) {
        this.bytesReceived = bytesReceived;
    }

    /**
     * 
     * 
     * @return
     *     The bytesSent
     */
    @JsonProperty("bytes_sent")
    public Long getBytesSent() {
        return bytesSent;
    }

    /**
     * 
     * 
     * @param bytesSent
     *     The bytes_sent
     */
    @JsonProperty("bytes_sent")
    public void setBytesSent(Long bytesSent) {
        this.bytesSent = bytesSent;
    }

    /**
     * 
     * 
     * @return
     *     The packetsReceived
     */
    @JsonProperty("packets_received")
    public Long getPacketsReceived() {
        return packetsReceived;
    }

    /**
     * 
     * 
     * @param packetsReceived
     *     The packets_received
     */
    @JsonProperty("packets_received")
    public void setPacketsReceived(Long packetsReceived) {
        this.packetsReceived = packetsReceived;
    }

    /**
     * 
     * 
     * @return
     *     The packetsSent
     */
    @JsonProperty("packets_sent")
    public Long getPacketsSent() {
        return packetsSent;
    }

    /**
     * 
     * 
     * @param packetsSent
     *     The packets_sent
     */
    @JsonProperty("packets_sent")
    public void setPacketsSent(Long packetsSent) {
        this.packetsSent = packetsSent;
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
