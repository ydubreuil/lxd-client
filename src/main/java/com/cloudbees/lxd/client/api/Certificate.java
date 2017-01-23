
package com.cloudbees.lxd.client.api;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
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
    "certificate",
    "fingerprint"
})
public class Certificate extends CertificatePut
{

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
    @JsonProperty("fingerprint")
    private String fingerprint;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Certificate() {
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
     *     The fingerprint
     */
    @JsonProperty("fingerprint")
    public String getFingerprint() {
        return fingerprint;
    }

    /**
     *
     *
     * @param fingerprint
     *     The fingerprint
     */
    @JsonProperty("fingerprint")
    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}
