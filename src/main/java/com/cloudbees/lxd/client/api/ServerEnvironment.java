
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
    "architectures",
    "certificate",
    "certificate_fingerprint",
    "driver",
    "driver_version",
    "kernel",
    "kernel_architecture",
    "kernel_version",
    "server",
    "server_pid",
    "server_version",
    "storage",
    "storage_version"
})
public class ServerEnvironment implements Serializable
{

    /**
     * 
     * 
     */
    @JsonProperty("addresses")
    private List<String> addresses = new ArrayList<String>();
    /**
     * 
     * 
     */
    @JsonProperty("architectures")
    private List<String> architectures = new ArrayList<String>();
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
    @JsonProperty("certificate_fingerprint")
    private String certificateFingerprint;
    /**
     * 
     * 
     */
    @JsonProperty("driver")
    private String driver;
    /**
     * 
     * 
     */
    @JsonProperty("driver_version")
    private String driverVersion;
    /**
     * 
     * 
     */
    @JsonProperty("kernel")
    private String kernel;
    /**
     * 
     * 
     */
    @JsonProperty("kernel_architecture")
    private String kernelArchitecture;
    /**
     * 
     * 
     */
    @JsonProperty("kernel_version")
    private String kernelVersion;
    /**
     * 
     * 
     */
    @JsonProperty("server")
    private String server;
    /**
     * 
     * 
     */
    @JsonProperty("server_pid")
    private Integer serverPid;
    /**
     * 
     * 
     */
    @JsonProperty("server_version")
    private String serverVersion;
    /**
     * 
     * 
     */
    @JsonProperty("storage")
    private String storage;
    /**
     * 
     * 
     */
    @JsonProperty("storage_version")
    private String storageVersion;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ServerEnvironment() {
    }

    /**
     * 
     * @param architectures
     * @param certificateFingerprint
     * @param server
     * @param addresses
     * @param serverVersion
     * @param storageVersion
     * @param kernel
     * @param certificate
     * @param kernelArchitecture
     * @param storage
     * @param serverPid
     * @param driver
     * @param driverVersion
     * @param kernelVersion
     */
    public ServerEnvironment(List<String> addresses, List<String> architectures, String certificate, String certificateFingerprint, String driver, String driverVersion, String kernel, String kernelArchitecture, String kernelVersion, String server, Integer serverPid, String serverVersion, String storage, String storageVersion) {
        this.addresses = addresses;
        this.architectures = architectures;
        this.certificate = certificate;
        this.certificateFingerprint = certificateFingerprint;
        this.driver = driver;
        this.driverVersion = driverVersion;
        this.kernel = kernel;
        this.kernelArchitecture = kernelArchitecture;
        this.kernelVersion = kernelVersion;
        this.server = server;
        this.serverPid = serverPid;
        this.serverVersion = serverVersion;
        this.storage = storage;
        this.storageVersion = storageVersion;
    }

    /**
     * 
     * 
     * @return
     *     The addresses
     */
    @JsonProperty("addresses")
    public List<String> getAddresses() {
        return addresses;
    }

    /**
     * 
     * 
     * @param addresses
     *     The addresses
     */
    @JsonProperty("addresses")
    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    /**
     * 
     * 
     * @return
     *     The architectures
     */
    @JsonProperty("architectures")
    public List<String> getArchitectures() {
        return architectures;
    }

    /**
     * 
     * 
     * @param architectures
     *     The architectures
     */
    @JsonProperty("architectures")
    public void setArchitectures(List<String> architectures) {
        this.architectures = architectures;
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
     *     The certificateFingerprint
     */
    @JsonProperty("certificate_fingerprint")
    public String getCertificateFingerprint() {
        return certificateFingerprint;
    }

    /**
     * 
     * 
     * @param certificateFingerprint
     *     The certificate_fingerprint
     */
    @JsonProperty("certificate_fingerprint")
    public void setCertificateFingerprint(String certificateFingerprint) {
        this.certificateFingerprint = certificateFingerprint;
    }

    /**
     * 
     * 
     * @return
     *     The driver
     */
    @JsonProperty("driver")
    public String getDriver() {
        return driver;
    }

    /**
     * 
     * 
     * @param driver
     *     The driver
     */
    @JsonProperty("driver")
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * 
     * 
     * @return
     *     The driverVersion
     */
    @JsonProperty("driver_version")
    public String getDriverVersion() {
        return driverVersion;
    }

    /**
     * 
     * 
     * @param driverVersion
     *     The driver_version
     */
    @JsonProperty("driver_version")
    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
    }

    /**
     * 
     * 
     * @return
     *     The kernel
     */
    @JsonProperty("kernel")
    public String getKernel() {
        return kernel;
    }

    /**
     * 
     * 
     * @param kernel
     *     The kernel
     */
    @JsonProperty("kernel")
    public void setKernel(String kernel) {
        this.kernel = kernel;
    }

    /**
     * 
     * 
     * @return
     *     The kernelArchitecture
     */
    @JsonProperty("kernel_architecture")
    public String getKernelArchitecture() {
        return kernelArchitecture;
    }

    /**
     * 
     * 
     * @param kernelArchitecture
     *     The kernel_architecture
     */
    @JsonProperty("kernel_architecture")
    public void setKernelArchitecture(String kernelArchitecture) {
        this.kernelArchitecture = kernelArchitecture;
    }

    /**
     * 
     * 
     * @return
     *     The kernelVersion
     */
    @JsonProperty("kernel_version")
    public String getKernelVersion() {
        return kernelVersion;
    }

    /**
     * 
     * 
     * @param kernelVersion
     *     The kernel_version
     */
    @JsonProperty("kernel_version")
    public void setKernelVersion(String kernelVersion) {
        this.kernelVersion = kernelVersion;
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

    /**
     * 
     * 
     * @return
     *     The serverPid
     */
    @JsonProperty("server_pid")
    public Integer getServerPid() {
        return serverPid;
    }

    /**
     * 
     * 
     * @param serverPid
     *     The server_pid
     */
    @JsonProperty("server_pid")
    public void setServerPid(Integer serverPid) {
        this.serverPid = serverPid;
    }

    /**
     * 
     * 
     * @return
     *     The serverVersion
     */
    @JsonProperty("server_version")
    public String getServerVersion() {
        return serverVersion;
    }

    /**
     * 
     * 
     * @param serverVersion
     *     The server_version
     */
    @JsonProperty("server_version")
    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    /**
     * 
     * 
     * @return
     *     The storage
     */
    @JsonProperty("storage")
    public String getStorage() {
        return storage;
    }

    /**
     * 
     * 
     * @param storage
     *     The storage
     */
    @JsonProperty("storage")
    public void setStorage(String storage) {
        this.storage = storage;
    }

    /**
     * 
     * 
     * @return
     *     The storageVersion
     */
    @JsonProperty("storage_version")
    public String getStorageVersion() {
        return storageVersion;
    }

    /**
     * 
     * 
     * @param storageVersion
     *     The storage_version
     */
    @JsonProperty("storage_version")
    public void setStorageVersion(String storageVersion) {
        this.storageVersion = storageVersion;
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
