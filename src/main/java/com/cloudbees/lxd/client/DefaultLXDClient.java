package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.Operation;
import com.cloudbees.lxd.client.api.ContainerAction;
import com.cloudbees.lxd.client.api.ContainerInfo;
import com.cloudbees.lxd.client.api.ContainerState;
import com.cloudbees.lxd.client.api.Device;
import com.cloudbees.lxd.client.api.ImageAliasesEntry;
import com.cloudbees.lxd.client.api.ImageInfo;
import com.cloudbees.lxd.client.api.LxdResponse;
import com.cloudbees.lxd.client.api.ResponseType;
import com.cloudbees.lxd.client.api.ServerState;
import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.lang.String.format;

public class DefaultLXDClient implements AutoCloseable {

    private static final String RECURSION_SUFFIX = "?recursion=1";

    protected final RequestContext ctx;

    public DefaultLXDClient() {
        this(Config.localAccessConfig());
    }

    public DefaultLXDClient(Config config) {
        this.ctx = new RequestContext(config);
    }

    @Override
    public void close() throws Exception {
        ctx.close();
    }

    public ServerState serverStatus() {
        return ctx.get("1.0").build()
            .execute()
            .expect(200).parseSync(new TypeReference<LxdResponse<ServerState>>() {});
    }

    public List<ContainerInfo> listContainers() {
        return ctx.get("1.0/containers" + RECURSION_SUFFIX).build()
            .execute()
            .expect(200).parseSync(new TypeReference<LxdResponse<List<ContainerInfo>>>() {});
    }

    public ContainerInfo containerInfo(String name) {
        return ctx.get(format("1.0/containers/%s", name)).build()
            .execute()
            .expect(200, 404).parseSync(new TypeReference<LxdResponse<ContainerInfo>>() {});
    }

    public ContainerState containerState(String name) {
        return ctx.get(format("1.0/containers/%s/state", name)).build()
            .execute()
            .expect(200, 404).parseSync(new TypeReference<LxdResponse<ContainerState>>() {});
    }

    /**
     *
     * @param name
     * @param imgremote either null for the local LXD daemon or one of remote name defined in {@link Config#remotesURL}
     * @param image
     * @param profiles
     * @param config
     * @param devices
     * @param ephem
     * @return
     */
    public LxdResponse<Operation> containerInit(String name, String imgremote, String image, List<String> profiles, Map<String, String> config, List<Device> devices, boolean ephem) {
        ServerState serverState = serverStatus();
        serverState.getEnvironment().getArchitectures();

        Map<String, String> source = new HashMap<>();
        source.put("type", "image");
        if (imgremote != null) {
            source.put("server", ctx.getConfig().getRemotesURL().get(imgremote));
            source.put("protocol", "simplestreams");
            // source.put("certificate", ); <= fetch the cert?
            source.put("fingerprint", image);
        } else {
            ImageAliasesEntry alias = imageGetAlias(image);
            String fingerprint = alias != null ? alias.getTarget() : image;
            ImageInfo imageInfo = imageInfo(fingerprint);
            if (imageInfo == null) {
                throw new LxdClientException("Unable to find image locally");
            }
            source.put("fingerprint", fingerprint);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("source", source);

        if (name != null && !name.isEmpty()) {
            body.put("name", name);
        }
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

        return ctx.post("1.0/containers")
            .jsonBody(body).build()
            .execute()
            .expect(202).parseOperation(ResponseType.ASYNC);
    }

    public LxdResponse<Operation> containerAction(String name, ContainerAction action, int timeout, boolean force, boolean stateful) {
        Map<String, Object> body = new HashMap<>();
        body.put("action", action.getValue());
        body.put("timeout", timeout);
        body.put("force", force);

        switch (action) {
            case Start:
            case Stop:
                body.put("stateful", stateful);
        }

        return ctx.put(format("1.0/containers/%s/state", name))
            .jsonBody(body).build()
            .execute()
            .expect(202).parseOperation(ResponseType.ASYNC);
    }

    public LxdResponse<Operation> containerDelete(String name) {
        String[] slashSplitted = name.split("/", 1);
        String url = slashSplitted.length == 1 ? format("1.0/containers/%s", name) : format("containers/%s/snapshots/%s", slashSplitted[0], slashSplitted[1]);
        return ctx.delete(url).build().execute().expect(202).parseOperation(ResponseType.ASYNC);
    }

    public LxdResponse<Void> filePush(String containerName, String targetPath, int gid, int uid, String mode, RequestBody body) {
        return ctx
            .post(urlBuilder -> urlBuilder
                .addPathSegment("1.0/containers").addPathSegment(containerName).addPathSegment("files")
                .addEncodedQueryParameter("path", targetPath))
            .body(body)
            .build(requestBuilder -> requestBuilder
                .addHeader("X-LXD-type", "file")
                .addHeader("X-LXD-mode", mode)
                .addHeader("X-LXD-uid", String.valueOf(uid))
                .addHeader("X-LXD-gid", String.valueOf(gid)))
            .execute()
            .expect(200).parse(new TypeReference<LxdResponse<Void>>() {}, ResponseType.SYNC);
    }

    public LxdResponse<Void> filePush(String containerName, String targetPath, int gid, int uid, String mode, File file) {
        return filePush(containerName, targetPath, gid, uid, mode, RequestBody.create(MediaType.parse("application/octet-stream"), file));
    }

    public LxdResponse<Operation> waitForCompletion(LxdResponse<Operation> operationResponse) {
        return ctx.get(format("%s/wait", operationResponse.getOperationUrl())).build().execute().expect(200).parseOperation(null);
    }

    public List<ImageInfo> listImages() {
        return ctx.get("1.0/images" + RECURSION_SUFFIX).build().execute().parseSync(new TypeReference<LxdResponse<List<ImageInfo>>>() {});
    }

    public LxdResponse<Operation> imageDelete(String imageFingerprint) {
        return ctx.delete(format("1.0/images/%s", imageFingerprint)).build().execute().expect(202).parseOperation(ResponseType.ASYNC);
    }

    public ImageInfo imageInfo(String imageFingerprint) {
        return ctx.get(format("1.0/images/%s", imageFingerprint)).build().execute().parseSync(new TypeReference<LxdResponse<ImageInfo>>() {});
    }

    public ImageAliasesEntry imageGetAlias(String aliasName) {
        return ctx.get(format("1.0/images/aliases/%s", aliasName)).build().execute().parseSync(new TypeReference<LxdResponse<ImageAliasesEntry>>() {});
    }

    private static final Logger logger = Logger.getLogger(DefaultLXDClient.class.getName());
}
