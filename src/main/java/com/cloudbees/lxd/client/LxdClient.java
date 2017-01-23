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

import com.cloudbees.lxd.client.api.Container;
import com.cloudbees.lxd.client.api.ContainerAction;
import com.cloudbees.lxd.client.api.ContainerState;
import com.cloudbees.lxd.client.api.Device;
import com.cloudbees.lxd.client.api.Image;
import com.cloudbees.lxd.client.api.ImageAliasesEntry;
import com.cloudbees.lxd.client.api.LxdResponse;
import com.cloudbees.lxd.client.api.Network;
import com.cloudbees.lxd.client.api.Operation;
import com.cloudbees.lxd.client.api.ResponseType;
import com.cloudbees.lxd.client.api.Server;
import com.cloudbees.lxd.client.api.StatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * Asynchronous LXD client based on RxJava.
 */
public class LxdClient implements AutoCloseable {

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    protected static final ObjectMapper JSON_MAPPER = new ObjectMapper()
        .enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)
        .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    private static final String RECURSION_SUFFIX = "?recursion=1";

    protected final RxOkHttpClientWrapper rxClient;
    protected final RxWsClientWrapper rxWsClient;

    public LxdClient() {
        this(Config.localAccessConfig());
    }

    public LxdClient(Config config) {
        this.rxClient = new RxOkHttpClientWrapper(config, new LxdResponseParser.Factory(JSON_MAPPER));
        this.rxWsClient = new RxWsClientWrapper(config);
    }

    @Override
    public void close() throws Exception {
        rxClient.close();
        rxWsClient.close();
    }

    /**
     * @return Server configuration and environment information
     */
    public Single<Server> server() {
        return rxClient.get("1.0").build()
            .flatMap(rp -> rp.parseSyncSingle(new TypeReference<LxdResponse<Server>>() {}));
    }

    /**
     * @return List of existing containers
     */
    public Single<List<Container>> containers() {
        return rxClient.get("1.0/containers" + RECURSION_SUFFIX).build()
            .flatMap(rp -> rp.parseSyncSingle(new TypeReference<LxdResponse<List<Container>>>() {}));
    }

    public ContainerClient container(String name) {
        return new ContainerClient(name);
    }

    public class ContainerClient {
        final String containerName;

        /**
         * Returns an API to interact with container containerName
         * @param containerName the name of the container. Should be 64 chars max, ASCII, no slash, no colon and no comma
         */
        ContainerClient(String containerName) {
            this.containerName = containerName;
        }

        /**
         * Change the container state
         * @param action State change action
         * @param timeout A timeout after which the state change is considered as failed
         * @param force Force the state change (currently only valid for stop and restart where it means killing the container)
         * @param stateful Whether to store or restore runtime state before stopping or starting (only valid for stop and start, defaults to false)
         * @return
         */
        protected Completable action(ContainerAction action, int timeout, boolean force, boolean stateful) {
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
                    .flatMap(rp -> Single.just(rp.parseOperation(ResponseType.ASYNC, 202)))
                .flatMapCompletable(o -> waitForCompletion(o));
        }

        public Completable delete() {
            return rxClient.delete(format("1.0/containers/%s", containerName)).build()
                .flatMap(rp -> Single.just(rp.parseOperation(ResponseType.ASYNC, 202)))
                .flatMapCompletable(o -> waitForCompletion(o));
        }

        public Completable deleteSnapshot(String snapshotName) {
            return rxClient.delete(format("1.0/containers/%s/snapshots/%s", containerName, snapshotName)).build()
                .flatMap(rp -> Single.just(rp.parseOperation(ResponseType.ASYNC, 202)))
                .flatMapCompletable(o -> waitForCompletion(o));
        }

        /**
         * Execute a command in a container
         * @param commands
         * @param environment
         * @param stdin Standard input or null if empty
         * @param stdout Standard output or null to discard
         * @param stderr Standard error or null to discard
         * @return
         */
        public Maybe<Integer> execute(List<String> commands, Map<String, String> environment, InputStream stdin, OutputStream stdout, OutputStream stderr) {
            Map<String, Object> body = new HashMap<>();
            body.put("command", commands);
            body.put("environment", environment);
            body.put("wait-for-websocket", true);
            body.put("interactive", false);

            return rxClient.post(format("1.0/containers/%s/exec", containerName), json(body)).build()
                .flatMap(rp -> Single.just(rp.parseOperation(ResponseType.ASYNC, 202)))
                .flatMapMaybe(response -> {
                    Map<String, String> fds = (Map<String, String>) response.getData().getMetadata().get("fds");

                    String stdinWsUrl = format("%s/websocket?secret=%s", response.getOperationUrl(), fds.get("0"));
                    Completable stdinWs = rxWsClient.wsCall(stdinWsUrl, stdin != null ? stdin : new ByteArrayInputStream(new byte[]{}), null);

                    String stdoutWsUrl = format("%s/websocket?secret=%s", response.getOperationUrl(), fds.get("1"));
                    Completable stdoutWs = rxWsClient.wsCall(stdoutWsUrl, null, stdout);

                    String stderrWsUrl = format("%s/websocket?secret=%s", response.getOperationUrl(), fds.get("2"));
                    Completable stderrWs = rxWsClient.wsCall(stderrWsUrl, null, stderr);

                    return Completable.mergeArray(stdinWs, stdoutWs, stderrWs)
                        .andThen(rxClient.get(format("%s/wait", response.getOperationUrl())).build())
                        .flatMapMaybe(rp -> {
                            Operation op = rp.parseOperation(ResponseType.SYNC, 200).getData();
                            Object processExitCode = op.getMetadata().get("return");
                            return Maybe.just((Integer) processExitCode);
                        });
                });
        }

        public Maybe<Container> info() {
            return rxClient.get(format("1.0/containers/%s", containerName)).build()
                .flatMapMaybe(rp -> rp.parseSyncMaybe(new TypeReference<LxdResponse<Container>>() {}));
        }

        /**
         * Create a new container
         * @param imgremote either null for the local LXD daemon or one of remote name defined in {@link Config#remotesURL}
         * @param image
         * @param profiles
         * @param config Config override
         * @param devices Optional list of devices the container should have
         * @param ephem Whether to destroy the container on shutdown
         * @return
         */
        public Completable init(String imgremote, String image, List<String> profiles, Map<String, String> config, List<Device> devices, boolean ephem) {

            Map<String, String> source = new HashMap<>();
            source.put("type", "image");
            if (imgremote != null) {
                source.put("server", rxClient.getConfig().getRemotesURL().get(imgremote));
                source.put("protocol", "simplestreams");
                // source.put("certificate", ); <= fetch the cert?
                source.put("fingerprint", image);
            } else {
                throw new IllegalArgumentException();
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
                .flatMap(rp -> Single.just(rp.parseOperation(ResponseType.ASYNC, 202)))
                .flatMapCompletable(o -> waitForCompletion(o));
        }

        public Maybe<InputStream> log(String fileName) {
            throw new UnsupportedOperationException();
        }

        public Completable rename(String newName) {
            Map<String, Object> body = new HashMap<>();
            body.put("name", newName);

            return rxClient.post(format("1.0/containers/%s", containerName), json(body)).build()
                .flatMap(rp -> Single.just(rp.parseOperation(ResponseType.ASYNC, 202)))
                .flatMapCompletable(o -> waitForCompletion(o));
        }

        public Completable renameSnapshot(String name, String newName) {
            Map<String, Object> body = new HashMap<>();
            body.put("name", newName);

            return rxClient.post(format("1.0/containers/%s/snapshots/%s", containerName, name), json(body)).build()
                .flatMap(rp -> Single.just(rp.parseOperation(ResponseType.ASYNC, 202)))
                .flatMapCompletable(o -> waitForCompletion(o));
        }

        /**
         * Starts the container. Does nothing if the container is already started.
         * Before issuing the start request, container state is checked and start request is issued only if
         * state is Stopped
         *
         * @return
         */
        public Completable start() {
            return action(ContainerAction.Start, 0, false, false);
        }

        public Maybe<ContainerState> state() {
            return rxClient.get(format("1.0/containers/%s", containerName)).build()
               .flatMapMaybe(rp -> rp.parseSyncMaybe(new TypeReference<LxdResponse<ContainerState>>() {}));
        }

        /**
         * Stops a container
         * @param timeout A timeout after which the stop is considered as failed
         * @param force Force stop the container ie, kill the container
         * @param stateful Whether to store or restore runtime state before stopping or starting (only valid for stop and start, defaults to false)
         * @return
         */
        public Completable stop(int timeout, boolean force, boolean stateful) {
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
                .flatMapCompletable(rp -> rp.parse(new TypeReference<LxdResponse<Void>>() {}, ResponseType.SYNC, 200) != null ?
                    Completable.complete() : Completable.error(new LxdClientException("")));
        }

        public Completable filePush(String targetPath, int gid, int uid, String mode, File file) {
            return filePush(targetPath, gid, uid, mode, RequestBody.create(MediaType.parse("application/octet-stream"), file));
        }
    }

    public Single<List<Image>> images() {
        return rxClient.get("1.0/images" + RECURSION_SUFFIX).build()
            .flatMap(rp -> rp.parseSyncSingle(new TypeReference<LxdResponse<List<Image>>>(){}));
    }

    public ImageClient image(String imageFingerprint) {
        return new ImageClient(imageFingerprint);
    }

    public class ImageClient {
        final String imageFingerprint;

        ImageClient(String imageFingerprint) {
            this.imageFingerprint = imageFingerprint;
        }

        public Maybe<Image> info() {
            return rxClient.get(format("1.0/images/%s", imageFingerprint)).build()
                .flatMapMaybe(rp -> rp.parseSyncMaybe(new TypeReference<LxdResponse<Image>>(){}));
        }

        public Completable delete() {
            return rxClient.delete(format("1.0/images/%s", imageFingerprint)).build()
                .flatMap(rp -> Single.just(rp.parseOperation(ResponseType.ASYNC, 202)))
                .flatMapCompletable(o -> waitForCompletion(o));
        }
    }

    public Maybe<ImageAliasesEntry> alias(String aliasName) {
        return rxClient.get(format("1.0/images/aliases/%s", aliasName)).build()
            .flatMapMaybe(rp -> rp.parseSyncMaybe(new TypeReference<LxdResponse<ImageAliasesEntry>>(){}));
    }

    /**
     * @return List of existing networks
     */
    public Single<List<Network>> networks() {
        return rxClient.get("1.0/networks" + RECURSION_SUFFIX).build()
            .flatMap(rp -> rp.parseSyncSingle(new TypeReference<LxdResponse<List<Network>>>() {}));
    }

    public NetworkClient network(String name) {
        return new NetworkClient(name);
    }

    public class NetworkClient {
        final String networkName;

        /**
         * Returns an API to interact with a network
         * @param networkName the name of the network. Should be 64 chars max, ASCII, no slash, no colon and no comma
         */
        NetworkClient(String networkName) {
            this.networkName = networkName;
        }

        /**
         * Creates a new network bridge
         * @param config the network configuration
         * @return
         */
        public Completable create(HashMap<String, String> config) {
            Network network = new Network();
            network.setName(networkName);
            network.setConfig(config);
            network.setManaged(true);

            return rxClient.post("1.0/networks", json(network)).build()
                .flatMapCompletable(rp -> rp.parseSyncOperation(201));
        }

        public Completable delete() {
            return rxClient.delete(format("1.0/networks/%s", networkName)).build()
                .flatMapCompletable(rp -> rp.parseSyncOperation(200));
        }

        /**
         * @return information about the network
         */
        public Maybe<Network> info() {
            return rxClient.get(format("1.0/networks/%s", networkName)).build()
                .flatMapMaybe(rp -> rp.parseSyncMaybe(new TypeReference<LxdResponse<Network>>() {}));
        }

        /**
         * Update a new network bridge
         * @param config the network configuration
         * @return
         */
        public Completable update(HashMap<String, String> config) {
            Network network = new Network();
            network.setName(networkName);
            network.setConfig(config);
            network.setManaged(true);

            return rxClient.put(format("1.0/networks/%s", networkName), json(network)).build()
                .flatMapCompletable(rp -> rp.parseSyncOperation(200));
        }
    }

    /**
     * Polls LXD for operation completion
     * @param operationResponse
     * @return a stream of Operations
     */
    public Completable waitForCompletion(LxdResponse<Operation> operationResponse) {
        /*
           As explained in https://www.stgraber.org/2016/04/18/lxd-api-direct-interaction/
          "data about past operations disappears 5 seconds after they’re done."

          https://medium.com/@v.danylo/server-polling-and-retrying-failed-operations-with-retrofit-and-rxjava-8bcc7e641a5a#.9ji4311wi
         */
        return rxClient.get(format("%s/wait?timeout=5", operationResponse.getOperationUrl())).build()
            .flatMapObservable(rp -> Observable.just(rp.parseOperation(ResponseType.SYNC, 200).getData()))
            .repeat()
            .takeUntil(operation -> operation.getStatusCode() != StatusCode.Running)
            .lastOrError()
            .flatMapCompletable(operation -> operation.getStatusCode() == StatusCode.Success ? Completable.complete() : Completable.error(new LxdClientException("Failed to complete")));
    }

    protected RequestBody json(Object resource) {
        try {
            return RequestBody.create(MEDIA_TYPE_JSON, JSON_MAPPER.writeValueAsString(resource));
        } catch (JsonProcessingException e) {
            throw new LxdClientException(e);
        }
    }
}
