package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.ContainerInfo;
import com.cloudbees.lxd.client.api.ContainerState;
import com.cloudbees.lxd.client.api.ImageAliasesEntry;
import com.cloudbees.lxd.client.api.ImageInfo;
import com.cloudbees.lxd.client.api.LxdResponse;
import com.cloudbees.lxd.client.api.ResponseType;
import com.cloudbees.lxd.client.api.ServerState;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Maybe;
import io.reactivex.Single;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;


/**
 * Asynchronous LXD client based on RxJava.
 */
public class RxLxdClient implements AutoCloseable {

    protected static final ObjectMapper JSON_MAPPER = new ObjectMapper()
        .enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)
        .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

    private static final String RECURSION_SUFFIX = "?recursion=1";


    protected final RxOkHttpClientWrapper rxClient;

    public RxLxdClient() {
        this(Config.localAccessConfig());
    }

    public RxLxdClient(Config config) {
        this.rxClient = new RxOkHttpClientWrapper(config);
    }

    @Override
    public void close() throws Exception {
        rxClient.close();
    }

    public Single<ServerState> serverState() {
        return rxClient.get("1.0").build()
            .flatMap(rc -> parseSyncSingle(rc, new TypeReference<LxdResponse<ServerState>>() {}));
    }

    public Single<List<ContainerInfo>> containers() {
        return rxClient.get("1.0/containers" + RECURSION_SUFFIX).build()
            .flatMap(rc -> parseSyncSingle(rc, new TypeReference<LxdResponse<List<ContainerInfo>>>() {}));
    }

    public ContainerOperation container(String name) {
        return new ContainerOperation(name);
    }

    class ContainerOperation {
        final String name;

        ContainerOperation(String name) {
            this.name = name;
        }

        public Maybe<ContainerInfo> info() {
            return rxClient.get(format("1.0/containers/%s", name)).build()
                .flatMapMaybe(rc -> parseSyncMaybe(rc, new TypeReference<LxdResponse<ContainerInfo>>() {}));
        }

        public void init() {
        }

        public void start() {
        }

        public Maybe<ContainerState> state() {
            return rxClient.get(format("1.0/containers/%s", name)).build()
               .flatMapMaybe(rc -> parseSyncMaybe(rc, new TypeReference<LxdResponse<ContainerState>>() {}));
        }

        public void stop() {
        }
    }

    public Single<List<ImageInfo>> images() {
        return rxClient.get("1.0/images" + RECURSION_SUFFIX).build()
            .flatMap(rc -> parseSyncSingle(rc, new TypeReference<LxdResponse<List<ImageInfo>>>(){}));
    }

    public ImageOperation image(String imageFingerprint) {
        return new ImageOperation(imageFingerprint);
    }

    class ImageOperation {
        final String imageFingerprint;

        ImageOperation(String imageFingerprint) {
            this.imageFingerprint = imageFingerprint;
        }

        public Maybe<ImageInfo> info() {
            return rxClient.get(format("1.0/images/%s", imageFingerprint)).build()
                .flatMapMaybe(rc -> parseSyncMaybe(rc, new TypeReference<LxdResponse<ImageInfo>>(){}));
        }
    }

    public Maybe<ImageAliasesEntry> alias(String aliasName) {
        return rxClient.get(format("1.0/images/aliases/%s", aliasName)).build()
            .flatMapMaybe(rc -> parseSyncMaybe(rc, new TypeReference<LxdResponse<ImageAliasesEntry>>(){}));
    }

    private static final List<Integer> ACCEPTABLE_HTTP_CODE_MAYBE = Arrays.asList(200, 404);

    protected <T> Maybe<T> parseSyncMaybe(RxOkHttpClientWrapper.ResponseContext rc, TypeReference<LxdResponse<T>> typeReference) {
        return Maybe.just(parse(rc, typeReference, ResponseType.SYNC, ACCEPTABLE_HTTP_CODE_MAYBE).getData());
    }

    private static final List<Integer> ACCEPTABLE_HTTP_CODE_SINGLE = Arrays.asList(200);

    protected <T> Single<T> parseSyncSingle(RxOkHttpClientWrapper.ResponseContext rc, TypeReference<LxdResponse<T>> typeReference) {
        return Single.just(parse(rc, typeReference, ResponseType.SYNC, ACCEPTABLE_HTTP_CODE_SINGLE).getData());
    }

    protected <T> LxdResponse<T> parse(RxOkHttpClientWrapper.ResponseContext context, TypeReference<LxdResponse<T>> typeReference, ResponseType expectedResponseType, List<Integer> expectedHttpStatusCodes) {
        assertHttpResponseCodes(context, expectedHttpStatusCodes);
        LxdResponse<T> lxdResponse = null;
        try {
            // we do not use a stream here to get the jsonBody dumped by Jackson when somethings goes wrong
            String body = context.response.body().string();
            lxdResponse = JSON_MAPPER.readValue(body, typeReference);
        } catch (IOException e) {
            throw new LxdExceptionBuilder(context.call.request()).with(context.response).with(e).build();
        }
        if (lxdResponse.getType() == null || ResponseType.ERROR == lxdResponse.getType()) {
            for(int expectedStatusCode: expectedHttpStatusCodes) {
                if (lxdResponse.getErrorCode() == expectedStatusCode) {
                    return null;
                }
            }
            throw new LxdExceptionBuilder(context.call.request()).with(lxdResponse).build();
        }
        if (expectedResponseType != null && lxdResponse.getType() != expectedResponseType) {
            throw new LxdExceptionBuilder(context.call.request()).withMessage(String.format("got bad response type, expected %s got %s", expectedResponseType, lxdResponse.getType())).build();
        }
        return lxdResponse;
    }

    protected void assertHttpResponseCodes(RxOkHttpClientWrapper.ResponseContext context, List<Integer> expectedHttpStatusCodes) {
        int statusCode = context.response.code();
        if (expectedHttpStatusCodes.size() > 0) {
            for (int expected : expectedHttpStatusCodes) {
                if (statusCode == expected) {
                    return;
                }
            }
            throw new LxdExceptionBuilder(context.call.request()).with(context.response).build();
        }
    }

    static class LxdExceptionBuilder {
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
