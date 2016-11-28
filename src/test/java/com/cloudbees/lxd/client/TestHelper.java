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
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;

/**
 * This utility class creates boilerplate to write tests against a Mock LXD server
 */
public class TestHelper implements AutoCloseable {

    public final MockWebServer server;
    final HttpUrl baseUrl;

    TestHelper(MockWebServer server) throws IOException {
        this.server = server;
        server.start();
        baseUrl = server.url("/");
    }

    @Override
    public void close() throws IOException {
        server.shutdown();
    }

    public Config getConfig() {
        return Config.remoteAccessConfig(baseUrl.toString());
    }

    @FunctionalInterface
    public interface MockResponseBuilder {
        MockResponse apply(RecordedRequest request) throws IOException;
    }

    public static class Builder {
        final MockWebServer server = new MockWebServer();
        final Map<String, Queue<MockResponseBuilder>> urlDispatchers = new HashMap<>();

        public Builder dispatchJsonString(String targetUrl, String body) {
            return dispatchForUrl(targetUrl, r ->  buildMockForJson(new Buffer().writeUtf8(body), 200));
        }

        public Builder dispatchJsonFile(String targetUrl, String classpathResourcePath) throws IOException {
            return dispatchJsonFile(targetUrl, classpathResourcePath, 200);
        }

        public Builder dispatchJsonFile(String targetUrl, String classpathResourcePath, int responseCode) throws IOException {
            return dispatchForUrl(targetUrl, r -> buildMockForJson(fillBufferFromResource(classpathResourcePath), responseCode));
        }

        public static MockResponse buildMockForJson(Buffer body, int responseCode) throws IOException {
            return new MockResponse().setResponseCode(responseCode).setBody(body).setHeader("Content-Type", "application/json; charset=utf-8");
        }

        public Builder dispatchForUrl(String targetUrl, MockResponseBuilder builder) {
            Queue<MockResponseBuilder> dispatchQueue = urlDispatchers.get(targetUrl);
            if (dispatchQueue == null) {
                dispatchQueue = new LinkedList<>();
                urlDispatchers.put(targetUrl, dispatchQueue);
            }
            dispatchQueue.add(builder);
            return this;
        }

        public Buffer fillBufferFromResource(String classpathResourcePath) throws IOException {
            InputStream stream = getClass().getResourceAsStream(classpathResourcePath);
            if (stream == null) {
                throw new IllegalArgumentException("Resource "+classpathResourcePath+" not found in classpath");
            }
            return new Buffer().readFrom(stream);
        }

        public Dispatcher buildJsonDispatcher() {
            return new Dispatcher() {
                @Override
                public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                    // we currently don't check if the HTTP method used in the good one...
                    if (urlDispatchers.containsKey(request.getPath())) {
                        try {
                            return urlDispatchers.get(request.getPath()).poll().apply(request);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
            if (urlDispatchers.size() > 0) {
                server.setDispatcher(buildJsonDispatcher());
            }
            return new TestHelper(server);
        }
    }
}
