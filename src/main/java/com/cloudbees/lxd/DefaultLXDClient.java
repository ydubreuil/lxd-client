package com.cloudbees.lxd;

import com.cloudbees.lxd.api.ContainerInfo;
import com.cloudbees.lxd.api.LXDResponse;
import com.cloudbees.lxd.api.ServerState;
import com.cloudbees.lxd.utils.HttpUtils;
import com.cloudbees.lxd.utils.URLUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class DefaultLXDClient implements AutoCloseable {

    protected static final ObjectMapper JSON_MAPPER = new ObjectMapper()
        .enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)
        .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

    protected final OkHttpClient httpClient;
    protected final Config config;

    public DefaultLXDClient() {
        this(Config.localAccessConfig());
    }

    public DefaultLXDClient(Config config) {
        this.config = config;
        httpClient = HttpUtils.createHttpClient(config);
    }

    @Override
    public void close() {
        if (httpClient.connectionPool() != null) {
            httpClient.connectionPool().evictAll();
        }
        if (httpClient.dispatcher() != null &&
            httpClient.dispatcher().executorService() != null &&
            !httpClient.dispatcher().executorService().isShutdown()
            ) {
            httpClient.dispatcher().executorService().shutdown();
        }
    }

    public LXDResponse<ServerState> serverConfiguration() throws IOException {
        Request getRequest = new Request.Builder()
            .url(URLUtils.join(config.useUnixTransport() ? "http://localhost:80" : config.getRemoteApiUrl(), "1.0"))
            .build();
        Call call = httpClient.newCall(getRequest);
        Response response = call.execute();
        return JSON_MAPPER.readValue(response.body().byteStream(), new TypeReference<LXDResponse<ServerState>>() {});
    }

    public LXDResponse<List<ContainerInfo>> containers() throws IOException {
        Request getRequest = new Request.Builder()
            .url(URLUtils.join(config.useUnixTransport() ? "http://localhost:80" : config.getRemoteApiUrl(), "1.0/containers?recursion=1"))
            .build();
        Call call = httpClient.newCall(getRequest);
        Response response = call.execute();
        return JSON_MAPPER.readValue(response.body().byteStream(), new TypeReference<LXDResponse<List<ContainerInfo>>>() {});
    }

    private static final Logger logger = Logger.getLogger(DefaultLXDClient.class.getName());
}
