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

package com.cloudbees.lxd.client.utils;

import com.cloudbees.lxd.client.Config;
import com.cloudbees.lxd.client.utils.unix.UnixSocketFactory;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.Okio;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class HttpUtils {
    public static OkHttpClient createHttpClient(final Config config) {
        OkHttpClient.Builder httpClientBuilder = createHttpClientBuilder(config);

        // max timeout determined for /wait
        httpClientBuilder.readTimeout(10, TimeUnit.SECONDS);

        // Log requests
        if (config.getLogLevel() != null) {
            httpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(config.getLogLevel()));
        }

        return httpClientBuilder.build();
    }

    public static OkHttpClient createWsClient(final Config config) {
        OkHttpClient.Builder httpClientBuilder = createHttpClientBuilder(config);
        httpClientBuilder.readTimeout(300, TimeUnit.SECONDS);

        return httpClientBuilder.build();
    }

    public static OkHttpClient.Builder createHttpClientBuilder(final Config config) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        // Follow any redirects
        httpClientBuilder.followRedirects(true);
        httpClientBuilder.followSslRedirects(true);

        if (config.useUnixTransport()) {
            UnixSocketFactory socketFactory = new UnixSocketFactory(config.getUnixSocketPath());
            httpClientBuilder.socketFactory(socketFactory);
        } else if (config.getBaseURL().startsWith("https")) {

            // there's no point having a hostname verifier given that we already use mutual authentication with certificate
            httpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });

            try {
                TrustManager[] trustManagers = SSLUtils.trustManagers(config);
                KeyManager[] keyManagers = SSLUtils.keyManagers(config);
                SSLContext sslContext = SSLUtils.sslContext(keyManagers, trustManagers);

                httpClientBuilder.protocols(Arrays.asList(Protocol.HTTP_1_1));
                httpClientBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagers[0]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return httpClientBuilder;
    }
}
