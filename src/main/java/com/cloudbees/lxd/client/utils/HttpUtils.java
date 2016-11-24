package com.cloudbees.lxd.client.utils;

import com.cloudbees.lxd.client.Config;
import com.cloudbees.lxd.client.utils.unix.UnixSocketFactory;
import okhttp3.OkHttpClient;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.TimeUnit;

public class HttpUtils {
    public static OkHttpClient createHttpClient(final Config config) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        // Follow any redirects
        httpClientBuilder.followRedirects(true);
        httpClientBuilder.followSslRedirects(true);

        // max timeout determined for /wait
        httpClientBuilder.readTimeout(35, TimeUnit.SECONDS);

        if (config.useUnixTransport()) {
            UnixSocketFactory socketFactory = new UnixSocketFactory(config.getUnixSocketPath());
            httpClientBuilder.socketFactory(socketFactory);
        } else if (config.getBaseURL().startsWith("https")) {
            SSLContext sslContext = null;
            try {
                sslContext = SSLUtils.sslContext(config);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            httpClientBuilder.socketFactory(sslContext.getSocketFactory());
        }

        return httpClientBuilder.build();
    }
}
