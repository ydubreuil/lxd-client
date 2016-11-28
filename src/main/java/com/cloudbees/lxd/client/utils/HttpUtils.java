package com.cloudbees.lxd.client.utils;

import com.cloudbees.lxd.client.Config;
import com.cloudbees.lxd.client.utils.unix.UnixSocketFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.TimeUnit;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BASIC;

public class HttpUtils {
    public static OkHttpClient createHttpClient(final Config config) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        // Follow any redirects
        httpClientBuilder.followRedirects(true);
        httpClientBuilder.followSslRedirects(true);

        // max timeout determined for /wait
        httpClientBuilder.readTimeout(10, TimeUnit.SECONDS);

        // Log requests
        if (config.getLogLevel() != null) {
            httpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(config.getLogLevel()));
        }

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
