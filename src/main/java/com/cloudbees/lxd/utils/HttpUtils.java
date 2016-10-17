package com.cloudbees.lxd.utils;

import com.cloudbees.lxd.Config;
import com.cloudbees.lxd.utils.unix.UnixSocketFactory;
import okhttp3.OkHttpClient;

public class HttpUtils {
    private static final String UNIX_SCHEME = "unix";
    private static final String FILE_SCHEME = "file";

    public static OkHttpClient createHttpClient(final Config config) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        // Follow any redirects
        httpClientBuilder.followRedirects(true);
        httpClientBuilder.followSslRedirects(true);

        if (config.useUnixTransport()) {
            UnixSocketFactory socketFactory = new UnixSocketFactory(config.getUnixSocketPath());
            httpClientBuilder.socketFactory(socketFactory);
        } else {
            // httpClientBuilder.sslSocketFactory(SSLUtils.getSocketFactory(config), SSLUtils.getTrustManager(config));
        }

        return httpClientBuilder.build();
    }
}
