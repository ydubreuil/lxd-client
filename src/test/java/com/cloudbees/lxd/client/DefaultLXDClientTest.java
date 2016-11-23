package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.Operation;
import com.cloudbees.lxd.client.api.ContainerAction;
import com.cloudbees.lxd.client.api.ContainerInfo;
import com.cloudbees.lxd.client.api.ImageAliasesEntry;
import com.cloudbees.lxd.client.api.ImageInfo;
import com.cloudbees.lxd.client.api.LxdResponse;
import com.cloudbees.lxd.client.api.ServerState;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DefaultLXDClientTest {

    @Test
    public void serverStatusTest() throws Exception {
        try (
            TestHelper t = new TestHelper.Builder().dispatchJsonFile("/1.0", "serverState-trusted.json").build();
            DefaultLXDClient client = new DefaultLXDClient(t.getConfig())
        ) {
            ServerState serverState = client.serverStatus();

            RecordedRequest rr = t.server.takeRequest();
            assertEquals("/1.0", rr.getPath());

            assertEquals("1.0", serverState.getApiVersion());
            assertEquals("trusted", serverState.getAuth());
            assertEquals("lxc", serverState.getEnvironment().getDriver());
        }
    }

    @Test
    public void listImagesTest() throws Exception {
        try (
            TestHelper t = new TestHelper.Builder().dispatchJsonFile("/1.0/images?recursion=1", "listImages.json").build();
            DefaultLXDClient client = new DefaultLXDClient(t.getConfig())
        ) {
            List<ImageInfo> imageInfoList = client.listImages();

            assertEquals(1, imageInfoList.size());
            ImageInfo first = imageInfoList.get(0);
            assertEquals("6f25adef061c3f2186c6910bff8cacd0c63e1493e3f8b616e52eb84076890bd1", first.getFingerprint());
            assertEquals("https://cloud-images.ubuntu.com/releases", first.getUpdateSource().getServer());
            assertEquals("ubuntu", first.getAliases().get(0).getName());
        }
    }

    @Test
    public void imageInfoTest() throws Exception {
        try (
            TestHelper t = new TestHelper.Builder().dispatchJsonFile("/1.0/images/aliases/ubuntu", "images_aliases_ubuntu.json")
                .dispatchJsonFile("/1.0/images/6f25adef061c3f2186c6910bff8cacd0c63e1493e3f8b616e52eb84076890bd1", "images_ubuntu.json")
                .build();
            DefaultLXDClient client = new DefaultLXDClient(t.getConfig())
        ) {
            ImageAliasesEntry imageAliasesEntry = client.imageGetAlias("ubuntu");
            ImageInfo imageInfo = client.imageInfo(imageAliasesEntry.getTarget());

            assertEquals(imageAliasesEntry.getTarget(), imageInfo.getFingerprint());
        }
    }

    @Test
    public void containerStartTest() throws Exception {
        try (TestHelper t = new TestHelper.Builder().dispatchJsonFile("/1.0/containers/it-957d09c12a9", "operations/start/container.json")
            .dispatchJsonFile("/1.0/containers/it-957d09c12a9/state", "operations/start/state.json", 202)
            .dispatchJsonFile("/1.0/operations/f96471ce-5689-433b-b382-cd1f5fbc669c/wait", "operations/start/operation.json")
            .build();
            DefaultLXDClient client = new DefaultLXDClient(t.getConfig())
        ) {
            ContainerInfo containerInfo = client.containerInfo("it-957d09c12a9");
            assertEquals(Operation.Status.Stopped.getValue(), containerInfo.getStatusCode().intValue());
            LxdResponse<Operation> operation = client.containerAction("it-957d09c12a9", ContainerAction.Start, 0, false, false);
            LxdResponse<Operation> finishedOperation = client.waitForCompletion(operation);
            assertEquals(Operation.Status.Success.getValue(), finishedOperation.getStatusCode().intValue());
        }
    }
}
