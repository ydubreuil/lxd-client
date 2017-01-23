
package com.cloudbees.lxd.client.api;

import java.util.ArrayList;
import java.util.List;
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
    "ServerPut",
    "ServerUntrusted",
    "environment"
})
public class Server extends ServerPut
{

    /**
     *
     *
     */
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
    /**
     *
     *
     */
    @JsonProperty("environment")
    private ServerEnvironment environment;

    /**
     * No args constructor for use in serialization
     *
     */
    public Server() {
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

}
