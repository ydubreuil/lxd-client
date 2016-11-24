package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.ContainerAction;
import com.cloudbees.lxd.client.api.ContainerInfo;
import com.cloudbees.lxd.client.api.ContainerState;
import com.cloudbees.lxd.client.api.Device;
import com.cloudbees.lxd.client.api.ImageAliasesEntry;
import com.cloudbees.lxd.client.api.ImageInfo;
import com.cloudbees.lxd.client.api.LxdResponse;
import com.cloudbees.lxd.client.api.Operation;
import com.cloudbees.lxd.client.api.ResponseType;
import com.cloudbees.lxd.client.api.ServerState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.lang.String.format;

/**
 * Asynchronous LXD client based on RxJava.
 */
public class RxLxdClient implements AutoCloseable {

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

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
            .flatMap(cr -> parseSyncSingle(cr, new TypeReference<LxdResponse<ServerState>>() {}));
    }

    public Single<List<ContainerInfo>> containers() {
        return rxClient.get("1.0/containers" + RECURSION_SUFFIX).build()
            .flatMap(cr -> parseSyncSingle(cr, new TypeReference<LxdResponse<List<ContainerInfo>>>() {}));
    }

    public Container container(String name) {
        return new Container(name);
    }

    class Container {
        final String containerName;

        Container(String containerName) {
            this.containerName = containerName;
        }

        public Single<Operation> action(ContainerAction action, int timeout, boolean force, boolean stateful) {
            Map<String, Object> body = new HashMap<>();
            body.put("action", action.getValue());
            body.put("timeout", timeout);
            body.put("force", force);

            switch (action) {
                case Start:
                case Stop:
                    body.put("stateful", stateful);
            }

            return rxClient.put(format("1.0/containers/%s/state", containerName), json(body)).build()
                    .flatMap(rc -> Single.just(parseOperation(rc, ResponseType.ASYNC, 202)))
                    .flatMap(o -> waitForCompletion(o));
        }

        public Completable delete() {
            return rxClient.delete(format("1.0/containers/%s", containerName)).build()
                .flatMap(rc -> Single.just(parseOperation(rc, ResponseType.ASYNC, 202)))
                .flatMap(o -> waitForCompletion(o))
                .toCompletable();
        }

        public Completable deleteSnapshot(String snapshotName) {
            return rxClient.delete(format("1.0/containers/%s/snapshots/%s", containerName, snapshotName)).build()
                .flatMap(rc -> Single.just(parseOperation(rc, ResponseType.ASYNC, 202)))
                .flatMap(o -> waitForCompletion(o))
                .toCompletable();
        }

        public Maybe<ContainerInfo> info() {
            return rxClient.get(format("1.0/containers/%s", containerName)).build()
                .flatMapMaybe(rc -> parseSyncMaybe(rc, new TypeReference<LxdResponse<ContainerInfo>>() {}));
        }

        public Single<Operation> init(String imgremote, String image, List<String> profiles, Map<String, String> config, List<Device> devices, boolean ephem) {

            Map<String, String> source = new HashMap<>();
            source.put("type", "image");
            if (imgremote != null) {
                source.put("server", rxClient.getConfig().getRemotesURL().get(imgremote));
                source.put("protocol", "simplestreams");
                // source.put("certificate", ); <= fetch the cert?
                source.put("fingerprint", image);
            } else {
                throw new NotImplementedException();
                /*
                ImageAliasesEntry alias = imageGetAlias(image);
                String fingerprint = alias != null ? alias.getTarget() : image;
                ImageInfo imageInfo = imageInfo(fingerprint);
                if (imageInfo == null) {
                    throw new LxdClientException("Unable to find image locally");
                }
                source.put("fingerprint", fingerprint);
                */
            }

            Map<String, Object> body = new HashMap<>();
            body.put("source", source);
            body.put("name", containerName);

            if (profiles != null && !profiles.isEmpty()) {
                body.put("profiles", profiles);
            }
            if (config != null && !config.isEmpty()) {
                body.put("config", config);
            }
            if (devices != null && !devices.isEmpty()) {
                body.put("devices", devices);
            }
            if (ephem) {
                body.put("ephem", ephem);
            }

            return rxClient.post(format("1.0/containers", containerName), json(body)).build()
                .flatMap(rc -> Single.just(parseOperation(rc, ResponseType.ASYNC, 202)))
                .flatMap(o -> waitForCompletion(o));
        }

        public Single<Operation> start(int timeout, boolean force, boolean stateful) {
            return action(ContainerAction.Start, timeout, force, stateful);
        }

        public Maybe<ContainerState> state() {
            return rxClient.get(format("1.0/containers/%s", containerName)).build()
               .flatMapMaybe(rc -> parseSyncMaybe(rc, new TypeReference<LxdResponse<ContainerState>>() {}));
        }

        public Single<Operation> stop(int timeout, boolean force, boolean stateful) {
            return action(ContainerAction.Stop, timeout, force, stateful);
        }

        public Completable filePush(String targetPath, int gid, int uid, String mode, RequestBody body) {
            return rxClient
                .post(urlBuilder -> urlBuilder
                    .addPathSegment("1.0/containers").addPathSegment(containerName).addPathSegment("files")
                    .addEncodedQueryParameter("path", targetPath),
                    body)
                .build(requestBuilder -> requestBuilder
                    .addHeader("X-LXD-type", "file")
                    .addHeader("X-LXD-mode", mode)
                    .addHeader("X-LXD-uid", String.valueOf(uid))
                    .addHeader("X-LXD-gid", String.valueOf(gid)))
                .flatMap(rc -> parseSyncSingle(rc, new TypeReference<LxdResponse<Void>>() {})).toCompletable();
        }

        public Completable filePush(String targetPath, int gid, int uid, String mode, File file) {
            return filePush(targetPath, gid, uid, mode, RequestBody.create(MediaType.parse("application/octet-stream"), file));
        }
    }

    public Single<List<ImageInfo>> images() {
        return rxClient.get("1.0/images" + RECURSION_SUFFIX).build()
            .flatMap(cr -> parseSyncSingle(cr, new TypeReference<LxdResponse<List<ImageInfo>>>(){}));
    }

    public Image image(String imageFingerprint) {
        return new Image(imageFingerprint);
    }

    class Image {
        final String imageFingerprint;

        Image(String imageFingerprint) {
            this.imageFingerprint = imageFingerprint;
        }

        public Maybe<ImageInfo> info() {
            return rxClient.get(format("1.0/images/%s", imageFingerprint)).build()
                .flatMapMaybe(cr -> parseSyncMaybe(cr, new TypeReference<LxdResponse<ImageInfo>>(){}));
        }
    }

    public Maybe<ImageAliasesEntry> alias(String aliasName) {
        return rxClient.get(format("1.0/images/aliases/%s", aliasName)).build()
            .flatMapMaybe(cr -> parseSyncMaybe(cr, new TypeReference<LxdResponse<ImageAliasesEntry>>(){}));
    }

    /*
        Warning!!!!
        This blog post, https://www.stgraber.org/2016/04/18/lxd-api-direct-interaction/
        says that "data about past operations disappears 5 seconds after they’re done."
    */
    public Single<Operation> waitForCompletion(LxdResponse<Operation> operationResponse) {
        return rxClient.get(format("%s/wait?timeout=10", operationResponse.getOperationUrl())).build()
            .flatMap(rc -> Single.just(parseOperation(rc, null, 200).getData()));
    }

    protected RequestBody json(Object resource) {
        try {
            return RequestBody.create(MEDIA_TYPE_JSON, JSON_MAPPER.writeValueAsString(resource));
        } catch (JsonProcessingException e) {
            throw new LxdClientException(e);
        }
    }

    protected <T> Maybe<T> parseSyncMaybe(RxOkHttpClientWrapper.TupleCallResponse cr, TypeReference<LxdResponse<T>> typeReference) {
        return Maybe.just(parse(cr, typeReference, ResponseType.SYNC, 200, 404).getData());
    }

    protected <T> Single<T> parseSyncSingle(RxOkHttpClientWrapper.TupleCallResponse rc, TypeReference<LxdResponse<T>> typeReference) {
        return Single.just(parse(rc, typeReference, ResponseType.SYNC, 200).getData());
    }

    public LxdResponse<Operation> parseOperation(RxOkHttpClientWrapper.TupleCallResponse cr, ResponseType expectedResponseType, int... expectedHttpStatusCodes) {
        return parse(cr, new TypeReference<LxdResponse<Operation>>() {}, expectedResponseType, expectedHttpStatusCodes);
    }

    protected <T> LxdResponse<T> parse(RxOkHttpClientWrapper.TupleCallResponse cr, TypeReference<LxdResponse<T>> typeReference, ResponseType expectedResponseType, int... expectedHttpStatusCodes) {
        assertHttpResponseCodes(cr.call, cr.response, expectedHttpStatusCodes);
        LxdResponse<T> lxdResponse = null;
        try {
            // we do not use a stream here to get the jsonBody dumped by Jackson when something goes wrong
            String body = cr.response.body().string();
            lxdResponse = JSON_MAPPER.readValue(body, typeReference);
        } catch (IOException e) {
            throw new LxdExceptionBuilder(cr.call.request()).with(cr.response).with(e).build();
        }
        if (lxdResponse.getType() == null || ResponseType.ERROR == lxdResponse.getType()) {
            for(int expectedStatusCode: expectedHttpStatusCodes) {
                if (lxdResponse.getErrorCode() == expectedStatusCode) {
                    return null;
                }
            }
            throw new LxdExceptionBuilder(cr.call.request()).with(lxdResponse).build();
        }
        if (expectedResponseType != null && lxdResponse.getType() != expectedResponseType) {
            throw new LxdExceptionBuilder(cr.call.request()).withMessage(String.format("got bad response type, expected %s got %s", expectedResponseType, lxdResponse.getType())).build();
        }
        return lxdResponse;
    }

    protected void assertHttpResponseCodes(Call call, Response response, int... expectedHttpStatusCodes) {
        int statusCode = response.code();
        if (expectedHttpStatusCodes.length > 0) {
            for (int expected : expectedHttpStatusCodes) {
                if (statusCode == expected) {
                    return;
                }
            }
            throw new LxdExceptionBuilder(call.request()).with(response).build();
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
