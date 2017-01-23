
package com.cloudbees.lxd.client.api;

import javax.annotation.Generated;
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
    "name"
})
public class ImageAliasesEntry extends ImageAliasesEntryPut
{

    /**
     *
     *
     */
    @JsonProperty("name")
    private String name;

    /**
     * No args constructor for use in serialization
     *
     */
    public ImageAliasesEntry() {
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
}
