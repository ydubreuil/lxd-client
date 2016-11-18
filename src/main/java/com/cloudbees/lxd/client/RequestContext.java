package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.Operation;
import com.cloudbees.lxd.client.api.LxdResponse;
import com.cloudbees.lxd.client.api.ResponseType;
import com.cloudbees.lxd.client.utils.HttpUtils;
import com.cloudbees.lxd.client.utils.URLUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.function.Function;

public class RequestContext implements AutoCloseable {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    protected static final ObjectMapper JSON_MAPPER = new ObjectMapper()
        .enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)
        .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

    protected final OkHttpClient client;
    protected final Config config;
    protected final String rootApiUrl;

    public RequestContext(Config config) {
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

        public RequestExecutor build(Function<Request.Builder, Request.Builder> f) {
            return new RequestExecutor(f.apply(new Request.Builder().method(method, body)).addHeader("User-Agent", "LXD-Java-Client"), resourceUrl);
        }

        public RequestExecutor build() {
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
    }

    class RequestExecutor {
        final Request.Builder requestBuilder;

        RequestExecutor(Request.Builder requestBuilder, HttpUrl resourceUrl) {
            this.requestBuilder = requestBuilder;
            requestBuilder.url(resourceUrl);
        }

        public Call newCall() {
            Request request = requestBuilder.build();
            return client.newCall(request);
        }

        public ResponseParser execute() {
            try {
                Call call = newCall();
                return new ResponseParser(call.request(), call.execute());
            } catch (IOException e) {
                throw new LxdClientException(e);
            }
        }
    }

    class ResponseParser {
        final Request request;
        final Response response;
        final ArrayList<Integer> expectedHttpStatusCodes = new ArrayList<>();

        ResponseParser(Request request, Response response) {
            this.request = request;
            this.response = response;
        }

        public ResponseParser expect(int... expectedStatusCodes) {
            for(int expectedStatusCode: expectedStatusCodes) {
                expectedHttpStatusCodes.add(expectedStatusCode);
            }
            return this;
        }

        public <T> LxdResponse<T> parse(TypeReference<LxdResponse<T>> typeReference, ResponseType expectedResponseType) {
            assertHttpResponseCodes();
            LxdResponse<T> lxdResponse = null;
            try {
                // we do not use a stream here to get the jsonBody dumped by Jackson when somethings goes wrong
                String body = response.body().string();
                lxdResponse = JSON_MAPPER.readValue(body, typeReference);
            } catch (IOException e) {
                throw new LxdExceptionBuilder(request).with(response).with(e).build();
            }
            if (lxdResponse.getType() == null || ResponseType.ERROR == lxdResponse.getType()) {
                for(int expectedStatusCode: expectedHttpStatusCodes) {
                    if (lxdResponse.getErrorCode() == expectedStatusCode) {
                        return null;
                    }
                }
                throw new LxdExceptionBuilder(request).with(lxdResponse).build();
            }
            if (expectedResponseType != null && lxdResponse.getType() != expectedResponseType) {
                throw new LxdExceptionBuilder(request).withMessage(String.format("got bad response type, expected %s got %s", expectedResponseType, lxdResponse.getType())).build();
            }
            return lxdResponse;
        }

        public LxdResponse<Operation> parseOperation(ResponseType expectedResponseType) {
            return parse(new TypeReference<LxdResponse<Operation>>() {}, expectedResponseType);
        }

        public  <T> T parseSync(TypeReference<LxdResponse<T>> typeReference) {
            return parse(typeReference, ResponseType.SYNC).getData();
        }

        protected void assertHttpResponseCodes() {
            int statusCode = response.code();
            if (expectedHttpStatusCodes.size() > 0) {
                for (int expected : expectedHttpStatusCodes) {
                    if (statusCode == expected) {
                        return;
                    }
                }
                throw new LxdExceptionBuilder(request).with(response).build();
            }
        }
    }

    private class LxdExceptionBuilder {
        final StringBuilder sb = new StringBuilder();
        Throwable throwable;

        LxdExceptionBuilder(Request request) {
            init(request);
        }

        void init(Request request) {
            sb.append("Failure executing: ").append(request.method())
                .append(" at: ").append(request.url()).append(".");
        }

        LxdExceptionBuilder with(Response response) {
            sb.append(" Status:").append(response.code()).append(".")
                .append(" Message: ").append(response.message()).append(".");
            try {
                String body = response.body().string();
                sb.append(" Body: ").append(body);
            } catch (Throwable t) {
                sb.append(" Body: <unreadable>");
            }

            return this;
        }

        LxdExceptionBuilder with(LxdResponse lxdResponse) {
            sb.append(" Status:").append(lxdResponse.getErrorCode())
                .append(" Message: ").append(lxdResponse.getError()).append(".");

            return this;
        }

        LxdExceptionBuilder withMessage(String message) {
            sb.append(" Message: ").append(message).append(".");

            return this;
        }

        LxdExceptionBuilder with(Throwable throwable) {
            this.throwable = throwable;

            return this;
        }

        public LxdClientException build() {
            return throwable == null ? new LxdClientException(sb.toString()) : new LxdClientException(sb.toString(), throwable);
        }
    }
}
