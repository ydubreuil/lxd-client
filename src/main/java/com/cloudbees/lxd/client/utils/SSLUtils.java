/*
 * Copyright (C) 2016 Original Authors
 * Copyright (C) 2016 Fabric8IO
 * Copyright (c) 2017 CloudBees, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.cloudbees.lxd.client.utils;

import com.cloudbees.lxd.client.Config;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

public class SSLUtils {
    public static TrustManager[] trustManagers(String certData) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        KeyStore trustStore = CertUtils.createTrustStore(certData);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        return tmf.getTrustManagers();
    }

    public static KeyManager[] keyManagers(String certData, String keyData, String algo, String passphrase) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException, CertificateException, InvalidKeySpecException, IOException {
        char[] passphraseChars = passphrase == null ? new char[]{} : passphrase.toCharArray();

        KeyStore keyStore = CertUtils.createKeyStore(certData, keyData, algo, passphraseChars);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, passphraseChars);
        return kmf.getKeyManagers();
    }

    public static TrustManager[] trustManagers(Config config) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        if (config.getServerPEMCert() != null || !config.getServerPEMCert().isEmpty()) {
        return trustManagers(config.getServerPEMCert());
        } else {
            return null;
        }
    }

    public static KeyManager[] keyManagers(Config config) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException, CertificateException, InvalidKeySpecException, IOException {
        return keyManagers(config.getClientPEMCert(), config.getClientPEMPrivateKey(), "RSA", config.getClientPEMKeyPassphrase());
    }

    public static SSLContext sslContext(KeyManager[] keyManagers, TrustManager[] trustManagers) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException, InvalidKeySpecException, KeyManagementException {
        // if we don't have a server certificate, we have to trust any certificate
        // chances that LXD uses an official certificate for HTTPS site are very low
        if (trustManagers == null) {
            trustManagers = new TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String s) {
                }

                public void checkServerTrusted(X509Certificate[] chain, String s) {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};
        }
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, trustManagers, new SecureRandom());
        return sslContext;
    }
}
