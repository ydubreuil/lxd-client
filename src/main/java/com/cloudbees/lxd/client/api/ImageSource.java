
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
    "alias",
    "certificate",
    "protocol",
    "server"
})
public class ImageSource implements Serializable
{

    /**
     * 
     * 
     */
    @JsonProperty("alias")
    private String alias;
    /**
     * 
     * 
     */
    @JsonProperty("certificate")
    private String certificate;
    /**
     * 
     * 
     */
    @JsonProperty("protocol")
    private String protocol;
    /**
     * 
     * 
     */
    @JsonProperty("server")
    private String server;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ImageSource() {
    }

    /**
     * 
     * @param server
     * @param protocol
     * @param certificate
     * @param alias
     */
    public ImageSource(String alias, String certificate, String protocol, String server) {
        this.alias = alias;
        this.certificate = certificate;
        this.protocol = protocol;
        this.server = server;
    }

    /**
     * 
     * 
     * @return
     *     The alias
     */
    @JsonProperty("alias")
    public String getAlias() {
        return alias;
    }

    /**
     * 
     * 
     * @param alias
     *     The alias
     */
    @JsonProperty("alias")
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * 
     * 
     * @return
     *     The certificate
     */
    @JsonProperty("certificate")
    public String getCertificate() {
        return certificate;
    }

    /**
     * 
     * 
     * @param certificate
     *     The certificate
     */
    @JsonProperty("certificate")
    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    /**
     * 
     * 
     * @return
     *     The protocol
     */
    @JsonProperty("protocol")
    public String getProtocol() {
        return protocol;
    }

    /**
     * 
     * 
     * @param protocol
     *     The protocol
     */
    @JsonProperty("protocol")
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * 
     * 
     * @return
     *     The server
     */
    @JsonProperty("server")
    public String getServer() {
        return server;
    }

    /**
     * 
     * 
     * @param server
     *     The server
     */
    @JsonProperty("server")
    public void setServer(String server) {
        this.server = server;
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
