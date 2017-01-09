/*
 * The MIT License
 *
 * Copyright (c) 2017 CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.cloudbees.lxd.client;

import okhttp3.logging.HttpLoggingInterceptor;

import java.util.HashMap;
import java.util.Map;

public class Config {

    private final String baseURL;
    private final String unixSocketPath;
    private final Map<String, String> remotesURL = new HashMap<>();

    private HttpLoggingInterceptor.Level logLevel = HttpLoggingInterceptor.Level.BODY;

    /** PEM encoded bytes of the client's certificate.
     * If {@link Config#baseURL} indicates a Unix socket, the certificate and key bytes will not be used. */
    final private String clientPEMCert;

    /** PEM encoded private bytes of the client's key associated with its certificate */
    final private String clientPEMPrivateKey;

    /** Password to decrypt the PEM encoded private bytes */
    final private String clientPEMPrivateKeyPassphrase;

    /** PEM encoded bytes of the server's certificate.
     * If {@link Config#baseURL} indicates a Unix socket, the certificate and key bytes will not be used. */
    final private String serverPEMCert;

    private Config(String baseURL, String unixSocketPath, String serverPEMCert, String clientPEMCert, String clientPEMPrivateKey, String clientPEMPrivateKeyPassphrase) {
        this.baseURL = baseURL;
        this.unixSocketPath = unixSocketPath;
        this.serverPEMCert = serverPEMCert;
        this.clientPEMCert = clientPEMCert;
        this.clientPEMPrivateKey = clientPEMPrivateKey;
        this.clientPEMPrivateKeyPassphrase = clientPEMPrivateKeyPassphrase;

        // from https://github.com/lxc/lxd/blob/34f62a7ea5cfea0f640ceb16ffc49a8f7c206c6c/config.go#L54
        remotesURL.put("images", "https://images.linuxcontainers.org");
        remotesURL.put("ubuntu", "https://cloud-images.ubuntu.com/releases");
        remotesURL.put("ubuntu-daily", "https://cloud-images.ubuntu.com/daily");
    }

    public String getBaseURL() {
        return baseURL;
    }

    public String getUnixSocketPath() {
        return unixSocketPath;
    }

    public boolean useUnixTransport() {
        return unixSocketPath != null;
    }

    public String getServerPEMCert() {
        return serverPEMCert;
    }

    public String getClientPEMCert() {
        return clientPEMCert;
    }

    public String getClientPEMPrivateKey() {
        return clientPEMPrivateKey;
    }

    public String getClientPEMKeyPassphrase() {
        return clientPEMPrivateKeyPassphrase;
    }

    public HttpLoggingInterceptor.Level getLogLevel() {
        return logLevel;
    }

    public Map<String, String> getRemotesURL() {
        return remotesURL;
    }

    public static Config localAccessConfig() {
        return new Config("http://localhost:80", "/var/lib/lxd/unix.socket", null, null, null, null);
    }
    public static Config localAccessConfig(String unixSocketPath) {
        return new Config("http://localhost:80", unixSocketPath, null, null, null, null);
    }

    public static Config remoteAccessConfig(String baseURL, String serverPEMCert, String clientPEMCert, String clientPEMPrivateKey, String clientPEMPrivateKeyPassphrase) {
        return new Config(baseURL, null, serverPEMCert, clientPEMCert, clientPEMPrivateKey, clientPEMPrivateKeyPassphrase);
    }

    public static Config remoteAccessConfig(String baseURL) {
        return new Config(baseURL, null, null, null, null, null);
    }
}
