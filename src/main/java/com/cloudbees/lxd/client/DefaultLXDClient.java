package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.AsyncOperation;
import com.cloudbees.lxd.client.api.ContainerInfo;
import com.cloudbees.lxd.client.api.ContainerState;
import com.cloudbees.lxd.client.api.Device;
import com.cloudbees.lxd.client.api.ImageAliasesEntry;
import com.cloudbees.lxd.client.api.ImageInfo;
import com.cloudbees.lxd.client.api.LxdResponse;
import com.cloudbees.lxd.client.api.ServerState;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.lang.String.format;

public class DefaultLXDClient implements AutoCloseable {

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
        return ctx.newGet(null).execute().parseSync(new TypeReference<LxdResponse<ServerState>>() {});
    }

    public List<ContainerInfo> listContainers() {
        return ctx.newGet("/containers?recursive=1").execute().parseSync(new TypeReference<LxdResponse<List<ContainerInfo>>>() {});
    }

    public ContainerInfo containerInfo(String name) {
        return ctx.newGet(format("/containers/%s", name)).expect(200, 404).execute().parseSync(new TypeReference<LxdResponse<ContainerInfo>>() {});
    }

    public ContainerState containerState(String name) {
        return ctx.newGet(format("containers/%s/state", name)).expect(200, 404).execute().parseSync(new TypeReference<LxdResponse<ContainerState>>() {});
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
    public AsyncOperation containerInit(String name, String imgremote, String image, List<String> profiles, Map<String, String> config, List<Device> devices, boolean ephem) {
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

        return ctx.newPost("containers", body).expect(202).execute().parseAsyncOperation();
    }

    public List<ImageInfo> listImages() {
        return ctx.newGet("images?recursive=1").execute().parseSync(new TypeReference<LxdResponse<List<ImageInfo>>>() {});
    }

    public ImageInfo imageInfo(String name) {
        return ctx.newGet(format("/images/%s", name)).execute().parseSync(new TypeReference<LxdResponse<ImageInfo>>() {});
    }

    public ImageAliasesEntry imageGetAlias(String name) {
        return ctx.newGet(format("/images/aliases/%s", name)).execute().parseSync(new TypeReference<LxdResponse<ImageAliasesEntry>>() {});
    }

    private static final Logger logger = Logger.getLogger(DefaultLXDClient.class.getName());
}
