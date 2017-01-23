
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
    "managed",
    "name",
    "type"
})
public class NetworksPost extends NetworkPut
{

    /**
     *
     *
     */
    @JsonProperty("managed")
    private Boolean managed;
    /**
     *
     *
     */
    @JsonProperty("name")
    private String name;
    /**
     *
     *
     */
    @JsonProperty("type")
    private String type;

    /**
     * No args constructor for use in serialization
     *
     */
    public NetworksPost() {
    }

    /**
     *
     *
     * @return
     *     The managed
     */
    @JsonProperty("managed")
    public Boolean getManaged() {
        return managed;
    }

    /**
     *
     *
     * @param managed
     *     The managed
     */
    @JsonProperty("managed")
    public void setManaged(Boolean managed) {
        this.managed = managed;
    }

    /**
     *
     *
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     *
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
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

}
