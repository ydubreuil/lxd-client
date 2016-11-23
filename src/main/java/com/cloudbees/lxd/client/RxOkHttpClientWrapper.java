package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.utils.HttpUtils;
import com.cloudbees.lxd.client.utils.URLUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.function.Function;

public class RxOkHttpClientWrapper implements AutoCloseable {

    protected static final ObjectMapper JSON_MAPPER = new ObjectMapper()
        .enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)
        .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_OCTET = MediaType.parse("application/octet-stream; charset=utf-8");
    public static final MediaType MEDIA_TYPE_TAR = MediaType.parse("application/tar; charset=utf-8");

    protected final OkHttpClient client;
    protected final Config config;
    protected final String rootApiUrl;

    public RxOkHttpClientWrapper(Config config) {
        this.config = config;
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

    public RequestBuilder post(String resourceUrl) {
        return new RequestBuilder(buildResourceUrl(resourceUrl), "POST");
    }

    public RequestBuilder post(Function<HttpUrl.Builder, HttpUrl.Builder> resourceUrlBuilder) {
        return new RequestBuilder(buildResourceUrl(resourceUrlBuilder), "POST");
    }

    public RequestBuilder put(String resourceUrl) {
        return new RequestBuilder(buildResourceUrl(resourceUrl), "PUT");
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

        public Single<ResponseContext> build(Function<Request.Builder, Request.Builder> f) {
            return newCall(f.apply(new Request.Builder().method(method, body)).addHeader("User-Agent", "LXD-Java-Client"));
        }

        public Single<ResponseContext> build() {
            return build(Function.identity());
        }

        public RequestBuilder jsonBody(Object resource) {
            try {
                body = RequestBody.create(MEDIA_TYPE_JSON, JSON_MAPPER.writeValueAsString(resource));
            } catch (JsonProcessingException e) {
                throw new LxdClientException(e);
            }
            return this;
        }

        public RequestBuilder body(RequestBody body) {
            this.body = body;
            return this;
        }

        public Single<ResponseContext> newCall(Request.Builder requestBuilder) {
            requestBuilder.url(resourceUrl);
            Request request = requestBuilder.build();

            return Single.create(s -> {
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call1, IOException e) {
                        s.onError(new CallException(call1, e));
                    }

                    @Override
                    public void onResponse(Call call1, Response response) throws IOException {
                        s.onSuccess(new ResponseContext(call1, response));
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

    public static class CallException extends Exception {
        public final Call call;

        private CallException(Call call, Throwable t) {
            super(t);
            this.call = call;
        }
    }

    public static class ResponseContext {
        public final Call call;
        public final Response response;

        private ResponseContext(Call call, Response response) {
            this.call = call;
            this.response = response;
        }
    }
}
