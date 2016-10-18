package com.cloudbees.lxd.client;

public class Config {

    private final String remoteApiUrl;
    private final String unixSocketPath;

    private boolean trustCerts;

    /** PEM encoded bytes of the client's certificate.
     * If {@link Config#remoteApiUrl} indicates a Unix socket, the certificate and key bytes will not be used. */
    private String clientPEMCert;

    /** PEM encoded private bytes of the client's key associated with its certificate */
    private String clientPEMKey;

    /** PEM encoded client certificate authority (if any) */
    private String clientPEMCa;

    private Config(String apiUri, String unixSocketPath, String clientPEMCert, String clientPEMKey, String clientPEMCa) {
        this.remoteApiUrl = apiUri;
        this.unixSocketPath = unixSocketPath;
        this.clientPEMCert = clientPEMCert;
        this.clientPEMKey = clientPEMKey;
        this.clientPEMCa = clientPEMCa;
    }

    public String getRemoteApiUrl() {
        return remoteApiUrl;
    }

    public String getUnixSocketPath() {
        return unixSocketPath;
    }

    public boolean useUnixTransport() {
        return unixSocketPath != null;
    }

    public boolean isTrustCerts() {
        return trustCerts;
    }

    public String getClientPEMCert() {
        return clientPEMCert;
    }

    public String getClientPEMKey() {
        return clientPEMKey;
    }

    public String getClientPEMCa() {
        return clientPEMCa;
    }

    public static Config localAccessConfig() {
        return new Config("http://localhost:80", "/var/lib/lxd/unix.socket", null, null, null);
    }
    public static Config localAccessConfig(String unixSocketPath) {
        return new Config("http://localhost:80", unixSocketPath, null, null, null);
    }

    public static Config remoteAccessConfig(String remoteUrl, String clientPEMCert, String clientPEMKey, String clientPEMCa) {
        return new Config(remoteUrl, null, clientPEMCert, clientPEMKey, clientPEMCa);
    }
}
