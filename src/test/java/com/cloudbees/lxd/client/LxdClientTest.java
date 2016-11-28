package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.ContainerInfo;
import com.cloudbees.lxd.client.api.ImageInfo;
import com.cloudbees.lxd.client.api.Operation;
import com.cloudbees.lxd.client.api.ServerState;
import io.reactivex.Observable;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class LxdClientTest {

    @Test
    public void serverStateTest() throws Exception {
        try (
            TestHelper t = new TestHelper.Builder().dispatchJsonFile("/1.0", "serverState-trusted.json").build();
            LxdClient client = new LxdClient(t.getConfig())
        ) {
            ServerState serverState = client.serverState().blockingGet();

            RecordedRequest rr = t.server.takeRequest();
            assertEquals("/1.0", rr.getPath());

            assertEquals("1.0", serverState.getApiVersion());
            assertEquals("trusted", serverState.getAuth());
            assertEquals("lxc", serverState.getEnvironment().getDriver());
        }
    }

    @Test
    public void serverStateFailingTest() throws Exception {
        try (
            TestHelper t = new TestHelper.Builder().dispatchForUrl("/1.0", r -> new MockResponse().setResponseCode(404)).build();
            LxdClient client = new LxdClient(t.getConfig())
        ) {
            try {
                client.serverState().blockingGet();
                fail();
            } catch (Exception e) {
                Assert.assertTrue(e instanceof LxdClientException);
            }
        }
    }

    @Test
    public void containerStartTest() throws Exception {
        try (TestHelper t = new TestHelper.Builder().dispatchJsonFile("/1.0/containers/it-957d09c12a9", "operations/start/container.json")
            .dispatchJsonFile("/1.0/containers/it-957d09c12a9/state", "operations/start/state.json", 202)
            .dispatchJsonFile("/1.0/operations/f96471ce-5689-433b-b382-cd1f5fbc669c/wait?timeout=5", "operations/start/operation-in-progress.json")
            .dispatchJsonFile("/1.0/operations/f96471ce-5689-433b-b382-cd1f5fbc669c/wait?timeout=5", "operations/start/operation.json")
            .build();
             LxdClient client = new LxdClient(t.getConfig())
        ) {
            LxdClient.Container container = client.container("it-957d09c12a9");
            ContainerInfo containerInfo = container.info().blockingGet();
            assertEquals(Operation.Status.Stopped.getValue(), containerInfo.getStatusCode().intValue());
            container.start().blockingAwait();
        }
    }

    @Test
    public void imagesListTest() throws Exception {
        try (
            TestHelper t = new TestHelper.Builder().dispatchJsonFile("/1.0/images?recursion=1", "listImages.json").build();
            LxdClient client = new LxdClient(t.getConfig())
        ) {
            List<ImageInfo> imageInfoList = client.images().blockingGet();

            assertEquals(1, imageInfoList.size());
            ImageInfo first = imageInfoList.get(0);
            assertEquals("6f25adef061c3f2186c6910bff8cacd0c63e1493e3f8b616e52eb84076890bd1", first.getFingerprint());
            assertEquals("https://cloud-images.ubuntu.com/releases", first.getUpdateSource().getServer());
            assertEquals("ubuntu", first.getAliases().get(0).getName());
        }
    }

    @Test
    public void rxTest() {
        AtomicInteger c = new AtomicInteger();
        Observable.just(5,3)
            .repeatWhen(o -> o.takeWhile(v -> c.getAndIncrement() == 0))
            .subscribe(System.out::println, Throwable::printStackTrace, () -> System.out.println("Done"));
    }
}
