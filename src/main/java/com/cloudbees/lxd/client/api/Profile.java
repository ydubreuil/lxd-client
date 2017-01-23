
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
    "name",
    "used_by"
})
public class Profile extends ProfilePut
{

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
    @JsonProperty("used_by")
    private List<String> usedBy = new ArrayList<String>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Profile() {
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
     *     The usedBy
     */
    @JsonProperty("used_by")
    public List<String> getUsedBy() {
        return usedBy;
    }

    /**
     *
     *
     * @param usedBy
     *     The used_by
     */
    @JsonProperty("used_by")
    public void setUsedBy(List<String> usedBy) {
        this.usedBy = usedBy;
    }

}
