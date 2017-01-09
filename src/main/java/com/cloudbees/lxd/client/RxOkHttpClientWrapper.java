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

import com.cloudbees.lxd.client.utils.HttpUtils;
import com.cloudbees.lxd.client.utils.URLUtils;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Make OkHttp client consumable using Rx
 */
public class RxOkHttpClientWrapper implements AutoCloseable {

    protected final OkHttpClient client;
    protected final Config config;
    protected final String rootApiUrl;
    protected final LxdResponseParser.Factory responseParserFactory;

    public RxOkHttpClientWrapper(Config config, LxdResponseParser.Factory responseParserFactory) {
        this.config = config;
        this.responseParserFactory = responseParserFactory;
        this.client = HttpUtils.createHttpClient(config);
        this.rootApiUrl = URLUtils.join(config.useUnixTransport() ? "http://localhost:80" : config.getBaseURL());
    }

    @Override
    public void close() {
        if (client.connectionPool() != null) {
            client.connectionPool().evictAll();
        }
        if (client.dispatcher() != null &&
            client.dispatcher().executorService() != null &&
            !client.dispatcher().executorService().isShutdown()
            ) {
            client.dispatcher().executorService().shutdown();
        }
    }

    public Config getConfig() {
        return config;
    }

    private HttpUrl buildResourceUrl(String resourceUrl) {
        return HttpUrl.parse(URLUtils.join(rootApiUrl, resourceUrl));
    }

    private HttpUrl buildResourceUrl(Function<HttpUrl.Builder, HttpUrl.Builder> resourceUrlBuilder) {
        return resourceUrlBuilder.apply(HttpUrl.parse(rootApiUrl).newBuilder()).build();
    }

    public RequestBuilder get(String resourceUrl) {
        return new RequestBuilder(buildResourceUrl(resourceUrl), "GET");
    }

    public RequestBuilder get(Function<HttpUrl.Builder, HttpUrl.Builder> resourceUrlBuilder) {
        return new RequestBuilder(buildResourceUrl(resourceUrlBuilder), "GET");
    }

    public RequestBuilder post(String resourceUrl, RequestBody body) {
        return new RequestBuilder(buildResourceUrl(resourceUrl), "POST", body);
    }

    public RequestBuilder post(Function<HttpUrl.Builder, HttpUrl.Builder> resourceUrlBuilder, RequestBody body) {
        return new RequestBuilder(buildResourceUrl(resourceUrlBuilder), "POST", body);
    }

    public RequestBuilder put(String resourceUrl, RequestBody body) {
        return new RequestBuilder(buildResourceUrl(resourceUrl), "PUT", body);
    }

    public RequestBuilder delete(String resourceUrl) {
        return new RequestBuilder(buildResourceUrl(resourceUrl), "DELETE");
    }

    class RequestBuilder {
        final String method;
        final HttpUrl resourceUrl;
        RequestBody body = null;

        RequestBuilder(HttpUrl resourceUrl, String method) {
            this.method = method;
            this.resourceUrl = resourceUrl;
        }

        RequestBuilder(HttpUrl resourceUrl, String method, RequestBody body) {
            this.method = method;
            this.resourceUrl = resourceUrl;
            this.body = body;
        }

        public Single<LxdResponseParser> build(Function<Request.Builder, Request.Builder> f) {
            return call(f.apply(new Request.Builder().method(method, body))
                .addHeader("User-Agent", "LXD-Java-Client")
                .url(resourceUrl));
        }

        public Single<LxdResponseParser> build() {
            return build(Function.identity());
        }

        public RequestBuilder body(RequestBody body) {
            this.body = body;
            return this;
        }

        protected Single<LxdResponseParser> call(Request.Builder requestBuilder) {
            Request request = requestBuilder.build();

            return Single.create(s -> {
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call1, IOException e) {
                        s.onError(new HttpException(call1, e));
                    }

                    @Override
                    public void onResponse(Call call1, Response response) throws IOException {
                        s.onSuccess(responseParserFactory.build(call1, response));
                    }
                });

                s.setDisposable(new Disposable() {
                    @Override
                    public void dispose() {
                        call.cancel();
                    }

                    @Override
                    public boolean isDisposed() {
                        return call.isCanceled();
                    }
                });
            });
        }
    }

    public static class HttpException extends Exception {
        public final Call call;

        private HttpException(Call call, Throwable t) {
            super(t);
            this.call = call;
        }
    }

    private static final Logger logger = Logger.getLogger(RxOkHttpClientWrapper.class.getName());
}
