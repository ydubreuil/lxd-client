package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.ServerState;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DefaultLXDClientTest {

    @Test
    public void serverStatusTest() throws IOException, InterruptedException {
        try (TestHelper t = new TestHelper.Builder().jsonDispatchResource("/1.0", "serverStatus-trusted.json").build()) {
            ServerState serverState = t.client.serverStatus();

            RecordedRequest request1 = t.server.takeRequest();
            assertEquals("/1.0", request1.getPath());

            assertEquals("1.0", serverState.getApiVersion());
            assertEquals("trusted", serverState.getAuth());
            assertEquals("lxc", serverState.getEnvironment().getDriver());
        }
    }
}
