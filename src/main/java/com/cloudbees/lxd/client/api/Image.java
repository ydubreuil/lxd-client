
package com.cloudbees.lxd.client.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
    "aliases",
    "architecture",
    "cached",
    "created_at",
    "expires_at",
    "filename",
    "fingerprint",
    "last_used_at",
    "size",
    "update_source",
    "uploaded_at"
})
public class Image extends ImagePut
{

    /**
     *
     *
     */
    @JsonProperty("aliases")
    private List<ImageAlias> aliases = new ArrayList<ImageAlias>();
    /**
     *
     *
     */
    @JsonProperty("architecture")
    private String architecture;
    /**
     *
     *
     */
    @JsonProperty("cached")
    private Boolean cached;
    /**
     *
     *
     */
    @JsonProperty("created_at")
    private Date createdAt;
    /**
     *
     *
     */
    @JsonProperty("expires_at")
    private Date expiresAt;
    /**
     *
     *
     */
    @JsonProperty("filename")
    private String filename;
    /**
     *
     *
     */
    @JsonProperty("fingerprint")
    private String fingerprint;
    /**
     *
     *
     */
    @JsonProperty("last_used_at")
    private Date lastUsedAt;
    /**
     *
     *
     */
    @JsonProperty("size")
    private Long size;
    /**
     *
     *
     */
    @JsonProperty("update_source")
    private ImageSource updateSource;
    /**
     *
     *
     */
    @JsonProperty("uploaded_at")
    private Date uploadedAt;

    /**
     * No args constructor for use in serialization
     *
     */
    public Image() {
    }

    /**
     *
     *
     * @return
     *     The aliases
     */
    @JsonProperty("aliases")
    public List<ImageAlias> getAliases() {
        return aliases;
    }

    /**
     *
     *
     * @param aliases
     *     The aliases
     */
    @JsonProperty("aliases")
    public void setAliases(List<ImageAlias> aliases) {
        this.aliases = aliases;
    }

    /**
     *
     *
     * @return
     *     The architecture
     */
    @JsonProperty("architecture")
    public String getArchitecture() {
        return architecture;
    }

    /**
     *
     *
     * @param architecture
     *     The architecture
     */
    @JsonProperty("architecture")
    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    /**
     *
     *
     * @return
     *     The cached
     */
    @JsonProperty("cached")
    public Boolean getCached() {
        return cached;
    }

    /**
     *
     *
     * @param cached
     *     The cached
     */
    @JsonProperty("cached")
    public void setCached(Boolean cached) {
        this.cached = cached;
    }

    /**
     *
     *
     * @return
     *     The createdAt
     */
    @JsonProperty("created_at")
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     *
     *
     * @param createdAt
     *     The created_at
     */
    @JsonProperty("created_at")
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     *
     * @return
     *     The expiresAt
     */
    @JsonProperty("expires_at")
    public Date getExpiresAt() {
        return expiresAt;
    }

    /**
     *
     *
     * @param expiresAt
     *     The expires_at
     */
    @JsonProperty("expires_at")
    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     *
     *
     * @return
     *     The filename
     */
    @JsonProperty("filename")
    public String getFilename() {
        return filename;
    }

    /**
     *
     *
     * @param filename
     *     The filename
     */
    @JsonProperty("filename")
    public void setFilename(String filename) {
        this.filename = filename;
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

    /**
     *
     *
     * @return
     *     The lastUsedAt
     */
    @JsonProperty("last_used_at")
    public Date getLastUsedAt() {
        return lastUsedAt;
    }

    /**
     *
     *
     * @param lastUsedAt
     *     The last_used_at
     */
    @JsonProperty("last_used_at")
    public void setLastUsedAt(Date lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    /**
     *
     *
     * @return
     *     The size
     */
    @JsonProperty("size")
    public Long getSize() {
        return size;
    }

    /**
     *
     *
     * @param size
     *     The size
     */
    @JsonProperty("size")
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     *
     *
     * @return
     *     The updateSource
     */
    @JsonProperty("update_source")
    public ImageSource getUpdateSource() {
        return updateSource;
    }

    /**
     *
     *
     * @param updateSource
     *     The update_source
     */
    @JsonProperty("update_source")
    public void setUpdateSource(ImageSource updateSource) {
        this.updateSource = updateSource;
    }

    /**
     *
     *
     * @return
     *     The uploadedAt
     */
    @JsonProperty("uploaded_at")
    public Date getUploadedAt() {
        return uploadedAt;
    }

    /**
     *
     *
     * @param uploadedAt
     *     The uploaded_at
     */
    @JsonProperty("uploaded_at")
    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
