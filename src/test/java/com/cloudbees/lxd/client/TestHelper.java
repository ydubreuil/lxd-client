package com.cloudbees.lxd.client;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

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

        public Builder jsonDispatchString(String targetUrl, String body) {
            server.setDispatcher(newJsonDispatcher(targetUrl, new Buffer().writeUtf8(body)));
            return this;
        }

        public Builder jsonDispatchResource(String targetUrl, String classpathResource) throws IOException {
            InputStream stream = getClass().getResourceAsStream(classpathResource);
            if (stream == null) {
                throw new IllegalArgumentException("Resource "+classpathResource+" not found in classpath");
            }
            server.setDispatcher(newJsonDispatcher(targetUrl, new Buffer().readFrom(stream)));
            return this;
        }

        public Dispatcher newJsonDispatcher(String targetUrl, Buffer body) {
            return new Dispatcher() {
                @Override
                public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                    if (request.getPath().equals(targetUrl)) {
                        return new MockResponse().setResponseCode(200).setBody(body).setHeader("Content-Type", "application/json; charset=utf-8");
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
            return new TestHelper(server);
        }
    }
}
