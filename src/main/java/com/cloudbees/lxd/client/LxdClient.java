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
import com.cloudbees.lxd.client.api.ContainerPut;
import com.cloudbees.lxd.client.api.ContainerState;
import com.cloudbees.lxd.client.api.Image;
import com.cloudbees.lxd.client.api.ImageAliasesEntry;
import com.cloudbees.lxd.client.api.LxdResponse;
import com.cloudbees.lxd.client.api.Network;
import com.cloudbees.lxd.client.api.Operation;
import com.cloudbees.lxd.client.api.Profile;
import com.cloudbees.lxd.client.api.ProfilePost;
import com.cloudbees.lxd.client.api.ProfilePut;
import com.cloudbees.lxd.client.api.ResponseType;
import com.cloudbees.lxd.client.api.Server;
import com.cloudbees.lxd.client.api.StatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<Server> server() {
        return rxClient.get("1.0").build()
            .flatMap(rp -> rp.parseSyncSingle(new TypeReference<LxdResponse<Server>>() {}));
    }

    /**
     * @return List of existing containers
     */
    public Mono<List<Container>> containers() {
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
        protected Mono<Void> action(ContainerAction action, int timeout, boolean force, boolean stateful) {
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
                .flatMap(rp -> Mono.just(rp.parseOperation(ResponseType.ASYNC, 202)))
                .flatMap(o -> waitForCompletion(o));
        }

        public Mono<Void> delete() {
            return rxClient.delete(format("1.0/containers/%s", containerName)).build()
                .flatMap(rp -> Mono.just(rp.parseOperation(ResponseType.ASYNC, 202)))
                .flatMap(o -> waitForCompletion(o));
        }

        public Mono<Void> deleteSnapshot(String snapshotName) {
            return rxClient.delete(format("1.0/containers/%s/snapshots/%s", containerName, snapshotName)).build()
                .flatMap(rp -> Mono.just(rp.parseOperation(ResponseType.ASYNC, 202)))
                .flatMap(o -> waitForCompletion(o));
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
        public Mono<Integer> execute(List<String> commands, Map<String, String> environment, InputStream stdin, OutputStream stdout, OutputStream stderr) {
            Map<String, Object> body = new HashMap<>();
            body.put("command", commands);
            body.put("environment", environment);
            body.put("wait-for-websocket", true);
            body.put("interactive", false);
 return null;
 /*
            return rxClient.post(format("1.0/containers/%s/exec", containerName), json(body)).build()
                .flatMap(rp -> Mono.just(rp.parseOperation(ResponseType.ASYNC, 202)))
                .flatMap(response -> {
                    Map<String, String> fds = (Map<String, String>) response.getData().getMetadata().get("fds");

                    String stdinWsUrl = format("%s/websocket?secret=%s", response.getOperationUrl(), fds.get("0"));
                    Mono<Void> stdinWs = rxWsClient.wsCall(stdinWsUrl, stdin != null ? stdin : new ByteArrayInputStream(new byte[]{}), null);

                    String stdoutWsUrl = format("%s/websocket?secret=%s", response.getOperationUrl(), fds.get("1"));
                    Mono<Void> stdoutWs = rxWsClient.wsCall(stdoutWsUrl, null, stdout);

                    String stderrWsUrl = format("%s/websocket?secret=%s", response.getOperationUrl(), fds.get("2"));
                    Mono<Void> stderrWs = rxWsClient.wsCall(stderrWsUrl, null, stderr);

                    return Mono.mergeArray(stdinWs, stdoutWs, stderrWs)
                        .andThen(rxClient.get(format("%s/wait", response.getOperationUrl())).build())
                        .flatMapMaybe(rp -> {
                            Operation op = rp.parseOperation(ResponseType.SYNC, 200).getData();
                            Object processExitCode = op.getMetadata().get("return");
                            return Mono.just((Integer) processExitCode);
                        });
                });*/
        }

        public Mono<Container> info() {
            return rxClient.get(format("1.0/containers/%s", containerName)).build()
                .flatMap(rp -> rp.parseSyncMaybe(new TypeReference<LxdResponse<Container>>() {}));
        }

        /**
         * Create a new container
         * @param imgremote either null for the local LXD daemon or one of remote name defined in {@link Config#remotes}
         * @param image
         * @param containerSpec specification of this new container
         * @return
         */
        public Mono<Void> init(String imgremote, String image, ContainerPut containerSpec) {

            Map<String, String> source = new HashMap<>();
            source.put("type", "image");
            if (imgremote != null) {
                Config.Remote remote = rxClient.getConfig().getRemotes().get(imgremote);
                if (remote == null) {
                    throw new IllegalArgumentException();
                }
                source.put("server", remote.getAddress());
                if (remote.getProtocol() != null) source.put("protocol", remote.getProtocol());
                source.put("fingerprint", image);
            } else {
                throw new IllegalArgumentException();
            }

            Map<String, Object> body = new HashMap<>();
            body.put("source", source);
            body.put("name", containerName);

            if (containerSpec.getProfiles() != null && !containerSpec.getProfiles().isEmpty()) {
                body.put("profiles", containerSpec.getProfiles());
            }
            if (containerSpec.getConfig() != null && !containerSpec.getConfig().isEmpty()) {
                body.put("config", containerSpec.getConfig());
            }
            if (containerSpec.getDevices() != null && !containerSpec.getDevices().isEmpty()) {
                body.put("devices", containerSpec.getDevices());
            }
            if (containerSpec.getEphemeral() != null) {
                body.put("ephem", containerSpec.getEphemeral().toString());
            }

            return rxClient.post(format("1.0/containers", containerName), json(body)).build()
                .flatMap(rp -> Mono.just(rp.parseOperation(ResponseType.ASYNC, 202)))
                .flatMap(o -> waitForCompletion(o));
        }

        public Mono<InputStream> log(String fileName) {
            throw new UnsupportedOperationException();
        }

        public Mono<Void> rename(String newName) {
            Map<String, Object> body = new HashMap<>();
            body.put("name", newName);

            return rxClient.post(format("1.0/containers/%s", containerName), json(body)).build()
                .flatMap(rp -> Mono.just(rp.parseOperation(ResponseType.ASYNC, 202)))
                .flatMap(o -> waitForCompletion(o));
        }

        public Mono<Void> renameSnapshot(String name, String newName) {
            Map<String, Object> body = new HashMap<>();
            body.put("name", newName);

            return rxClient.post(format("1.0/containers/%s/snapshots/%s", containerName, name), json(body)).build()
                .flatMap(rp -> Mono.just(rp.parseOperation(ResponseType.ASYNC, 202)))
                .flatMap(o -> waitForCompletion(o));
        }

        /**
         * Starts the container. Does nothing if the container is already started.
         * Before issuing the start request, container state is checked and start request is issued only if
         * state is Stopped
         *
         * @return
         */
        public Mono<Void> start() {
            return action(ContainerAction.Start, 0, false, false);
        }

        public Mono<ContainerState> state() {
            return rxClient.get(format("1.0/containers/%s", containerName)).build()
               .flatMap(rp -> rp.parseSyncMaybe(new TypeReference<LxdResponse<ContainerState>>() {}));
        }

        /**
         * Stops a container
         * @param timeout A timeout after which the stop is considered as failed
         * @param force Force stop the container ie, kill the container
         * @param stateful Whether to store or restore runtime state before stopping or starting (only valid for stop and start, defaults to false)
         * @return
         */
        public Mono<Void> stop(int timeout, boolean force, boolean stateful) {
            return action(ContainerAction.Stop, timeout, force, stateful);
        }

        public Mono<Void> update(ContainerPut containerSpec) {
            return rxClient.put(format("1.0/containers/%s", containerName), json(containerSpec)).build()
                .flatMap(rp -> rp.parseSyncOperation(200));
        }

        public Mono<Void> filePush(String targetPath, int gid, int uid, String mode, RequestBody body) {
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
                .flatMap(rp -> rp.parse(new TypeReference<LxdResponse<Void>>() {}, ResponseType.SYNC, 200) != null ?
                    Mono.empty() : Mono.error(new LxdClientException("")));
        }

        public Mono<Void> filePush(String targetPath, int gid, int uid, String mode, File file) {
            return filePush(targetPath, gid, uid, mode, RequestBody.create(MediaType.parse("application/octet-stream"), file));
        }
    }

    public Mono<List<Image>> images() {
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

        public Mono<Image> info() {
            return rxClient.get(format("1.0/images/%s", imageFingerprint)).build()
                .flatMap(rp -> rp.parseSyncMaybe(new TypeReference<LxdResponse<Image>>(){}));
        }

        public Mono<Void> delete() {
            return rxClient.delete(format("1.0/images/%s", imageFingerprint)).build()
                .flatMap(rp -> Mono.just(rp.parseOperation(ResponseType.ASYNC, 202)))
                .flatMap(o -> waitForCompletion(o));
        }
    }

    public Mono<ImageAliasesEntry> alias(String aliasName) {
        return rxClient.get(format("1.0/images/aliases/%s", aliasName)).build()
            .flatMap(rp -> rp.parseSyncMaybe(new TypeReference<LxdResponse<ImageAliasesEntry>>(){}));
    }

    /**
     * @return List of existing networks
     */
    public Mono<List<Network>> networks() {
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
         * Creates a new network
         * @param config the network configuration
         * @return
         */
        public Mono<Void> create(HashMap<String, String> config) {
            Network network = new Network();
            network.setName(networkName);
            network.setConfig(config);
            network.setManaged(true);

            return rxClient.post("1.0/networks", json(network)).build()
                .flatMap(rp -> rp.parseSyncOperation(201));
        }

        public Mono<Void> delete() {
            return rxClient.delete(format("1.0/networks/%s", networkName)).build()
                .flatMap(rp -> rp.parseSyncOperation(200));
        }

        /**
         * @return information about the network
         */
        public Mono<Network> info() {
            return rxClient.get(format("1.0/networks/%s", networkName)).build()
                .flatMap(rp -> rp.parseSyncMaybe(new TypeReference<LxdResponse<Network>>() {}));
        }

        /**
         * Rename a network
         * @param newName the new name of this network
         * @return
         */
        public Mono<Void> rename(String newName) {
            Map<String, Object> body = new HashMap<>();
            body.put("name", newName);

            return rxClient.post(format("1.0/networks/%s", networkName), json(body)).build()
                .flatMap(rp -> rp.parseSyncOperation(200));
        }

        /**
         * Update a network
         * @param config the network configuration
         * @return
         */
        public Mono<Void> update(HashMap<String, String> config) {
            Network network = new Network();
            network.setName(networkName);
            network.setConfig(config);
            network.setManaged(true);

            return rxClient.put(format("1.0/networks/%s", networkName), json(network)).build()
                .flatMap(rp -> rp.parseSyncOperation(200));
        }
    }


    /**
     * @return List of existing profiles
     */
    public Mono<List<Profile>> profiles() {
        return rxClient.get("1.0/profiles" + RECURSION_SUFFIX).build()
            .flatMap(rp -> rp.parseSyncSingle(new TypeReference<LxdResponse<List<Profile>>>() {}));
    }

    public ProfileClient profile(String name) {
        return new ProfileClient(name);
    }

    public class ProfileClient {
        final String profileName;

        /**
         * Returns an API to interact with a profile
         * @param profileName the name of the profile.
         */
        ProfileClient(String profileName) {
            this.profileName = profileName;
        }

        /**
         * Creates a new profile
         * @param profileSpec the profile configuration
         * @return
         */
        public Mono<Void> create(ProfilePut profileSpec) {
            ProfilePost body = new ProfilePost(profileName, profileSpec);

            return rxClient.post("1.0/profiles", json(body)).build()
                .flatMap(rp -> rp.parseSyncOperation(201));
        }

        public Mono<Void> delete() {
            return rxClient.delete(format("1.0/profiles/%s", profileName)).build()
                .flatMap(rp -> rp.parseSyncOperation(200));
        }

        /**
         * @return information about the profile
         */
        public Mono<Profile> info() {
            return rxClient.get(format("1.0/profiles/%s", profileName)).build()
                .flatMap(rp -> rp.parseSyncMaybe(new TypeReference<LxdResponse<Profile>>() {}));
        }

        /**
         * Rename a profile
         * @param newName the new name of this network
         * @return
         */
        public Mono<Void> rename(String newName) {
            Map<String, Object> body = new HashMap<>();
            body.put("name", newName);

            return rxClient.post(format("1.0/profiles/%s", profileName), json(body)).build()
                .flatMap(rp -> rp.parseSyncOperation(200));
        }

        /**
         * Update a profile
         * @param profileSpec the profile configuration
         * @return
         */
        public Mono<Void> update(ProfilePut profileSpec) {
            return rxClient.put(format("1.0/profiles/%s", profileName), json(profileSpec)).build()
                .flatMap(rp -> rp.parseSyncOperation(200));
        }
    }

    /**
     * Polls LXD for operation completion
     * @param operationResponse
     * @return a stream of Operations
     */
    public Mono<Void> waitForCompletion(LxdResponse<Operation> operationResponse) {
        /*
           As explained in https://www.stgraber.org/2016/04/18/lxd-api-direct-interaction/
          "data about past operations disappears 5 seconds after they’re done."

          https://medium.com/@v.danylo/server-polling-and-retrying-failed-operations-with-retrofit-and-rxjava-8bcc7e641a5a#.9ji4311wi
         */
        return rxClient.get(format("%s/wait?timeout=1", operationResponse.getOperationUrl())).build()
            .flatMapMany(rp -> Flux.just(rp.parseOperation(ResponseType.SYNC, 200).getData()))
            .repeat()
            .takeUntil(operation -> operation.getStatusCode() != StatusCode.Running)
            .last()
            .flatMap(operation -> operation.getStatusCode() == StatusCode.Success ? Mono.empty() : Mono.error(new LxdClientException("Failed to complete")));
    }

    protected RequestBody json(Object resource) {
        try {
            return RequestBody.create(MEDIA_TYPE_JSON, JSON_MAPPER.writeValueAsString(resource));
        } catch (JsonProcessingException e) {
            throw new LxdClientException(e);
        }
    }
}
