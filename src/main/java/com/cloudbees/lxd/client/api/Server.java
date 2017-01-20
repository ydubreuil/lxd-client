
package com.cloudbees.lxd.client.api;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Represents the state of a LXD server
 *
 * Note: cannot use auto-generated sources as 2 types (Server and ServerUntrusted) are inlined in the response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "api_extensions",
    "api_status",
    "api_version",
    "auth",
    "config",
    "environment",
    "public"
})
public class Server implements Serializable
{

    /**
     *
     *
     */

    @JsonProperty("config")
    private Map<String, Object> config;

    @JsonProperty("api_extensions")
    private List<String> apiExtensions = new ArrayList<String>();
    /**
     *
     *
     */
    @JsonProperty("api_status")
    private String apiStatus;
    /**
     *
     *
     */
    @JsonProperty("api_version")
    private String apiVersion;
    /**
     *
     *
     */
    @JsonProperty("auth")
    private String auth;
    /**
     *
     *
     */
    @JsonProperty("public")
    private Boolean _public;

    @JsonProperty("environment")
    private ServerEnvironment environment;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Server() {
    }

    /**
     *
     * @param apiVersion
     * @param auth
     * @param _public
     * @param apiExtensions
     * @param apiStatus
     */
    public Server(List<String> apiExtensions, String apiStatus, String apiVersion, String auth, Boolean _public) {
        this.apiExtensions = apiExtensions;
        this.apiStatus = apiStatus;
        this.apiVersion = apiVersion;
        this.auth = auth;
        this._public = _public;
    }

    /**
     *
     *
     * @return
     *     The apiExtensions
     */
    @JsonProperty("api_extensions")
    public List<String> getApiExtensions() {
        return apiExtensions;
    }

    /**
     *
     *
     * @param apiExtensions
     *     The api_extensions
     */
    @JsonProperty("api_extensions")
    public void setApiExtensions(List<String> apiExtensions) {
        this.apiExtensions = apiExtensions;
    }

    /**
     *
     *
     * @return
     *     The apiStatus
     */
    @JsonProperty("api_status")
    public String getApiStatus() {
        return apiStatus;
    }

    /**
     *
     *
     * @param apiStatus
     *     The api_status
     */
    @JsonProperty("api_status")
    public void setApiStatus(String apiStatus) {
        this.apiStatus = apiStatus;
    }

    /**
     *
     *
     * @return
     *     The apiVersion
     */
    @JsonProperty("api_version")
    public String getApiVersion() {
        return apiVersion;
    }

    /**
     *
     *
     * @param apiVersion
     *     The api_version
     */
    @JsonProperty("api_version")
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    /**
     *
     *
     * @return
     *     The auth
     */
    @JsonProperty("auth")
    public String getAuth() {
        return auth;
    }

    /**
     *
     *
     * @param auth
     *     The auth
     */
    @JsonProperty("auth")
    public void setAuth(String auth) {
        this.auth = auth;
    }

    /**
     *
     *
     * @return
     *     The config
     */
    @JsonProperty("config")
    public Map<String,Object> getConfig() {
        return config;
    }

    /**
     *
     *
     * @param config
     *     The config
     */
    @JsonProperty("config")
    public void setConfig(Map<String,Object> config) {
        this.config = config;
    }

    /**
     *
     *
     * @return
     *     The environment
     */
    @JsonProperty("environment")
    public ServerEnvironment getEnvironment() {
        return environment;
    }

    /**
     *
     *
     * @param environment
     *     The environment
     */
    @JsonProperty("environment")
    public void setEnvironment(ServerEnvironment environment) {
        this.environment = environment;
    }


    /**
     *
     *
     * @return
     *     The _public
     */
    @JsonProperty("public")
    public Boolean getPublic() {
        return _public;
    }

    /**
     *
     *
     * @param _public
     *     The public
     */
    @JsonProperty("public")
    public void setPublic(Boolean _public) {
        this._public = _public;
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
