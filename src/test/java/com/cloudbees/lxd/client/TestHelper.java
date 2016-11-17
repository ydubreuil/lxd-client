package com.cloudbees.lxd.client;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This utility class creates boilerplate to write tests against a Mock LXD server
 */
public class TestHelper implements AutoCloseable {

    public final MockWebServer server;
    public final DefaultLXDClient client;

    TestHelper(MockWebServer server) throws IOException {
        this.server = server;
        server.start();
        HttpUrl baseUrl = server.url("/");
        client = new DefaultLXDClient(Config.remoteAccessConfig(baseUrl.toString()));
    }

    @Override
    public void close() throws IOException {
        server.shutdown();
    }

    public static class Builder {
        final MockWebServer server = new MockWebServer();
        final Map<String, Buffer> dispatchedJsonData = new HashMap<>();

        public Builder dispatchJsonString(String targetUrl, String body) {
            dispatchedJsonData.put(targetUrl, new Buffer().writeUtf8(body));
            return this;
        }

        public Builder dispatchJsonFile(String targetUrl, String classpathResourcePath) throws IOException {
            InputStream stream = getClass().getResourceAsStream(classpathResourcePath);
            if (stream == null) {
                throw new IllegalArgumentException("Resource "+classpathResourcePath+" not found in classpath");
            }
            dispatchedJsonData.put(targetUrl, new Buffer().readFrom(stream));
            return this;
        }

        public Dispatcher buildJsonDispatcher() {
            return new Dispatcher() {
                @Override
                public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                    if (dispatchedJsonData.containsKey(request.getPath())) {
                        return new MockResponse().setResponseCode(200).setBody(dispatchedJsonData.get(request.getPath())).setHeader("Content-Type", "application/json; charset=utf-8");
                    }
                    return new MockResponse().setResponseCode(500);
                }
            };
        }

        public Builder server(Function<MockWebServer,Void> f) {
            f.apply(server);
            return this;
        }

        TestHelper build() throws IOException {
            if (dispatchedJsonData.size() > 0) {
                server.setDispatcher(buildJsonDispatcher());
            }
            return new TestHelper(server);
        }
    }
}
