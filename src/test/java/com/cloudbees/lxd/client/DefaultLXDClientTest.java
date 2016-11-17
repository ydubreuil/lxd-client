package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.ImageAliasesEntry;
import com.cloudbees.lxd.client.api.ImageInfo;
import com.cloudbees.lxd.client.api.ServerState;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DefaultLXDClientTest {

    @Test
    public void serverStatusTest() throws IOException, InterruptedException {
        try (TestHelper t = new TestHelper.Builder().dispatchJsonFile("/1.0", "serverStatus-trusted.json").build()) {
            ServerState serverState = t.client.serverStatus();

            RecordedRequest rr = t.server.takeRequest();
            assertEquals("/1.0", rr.getPath());

            assertEquals("1.0", serverState.getApiVersion());
            assertEquals("trusted", serverState.getAuth());
            assertEquals("lxc", serverState.getEnvironment().getDriver());
        }
    }

    @Test
    public void listImagesTest() throws IOException, InterruptedException {
        try (TestHelper t = new TestHelper.Builder().dispatchJsonFile("/1.0/images?recursion=1", "listImages.json").build()) {
            List<ImageInfo> imageInfoList = t.client.listImages();

            assertEquals(1, imageInfoList.size());
            ImageInfo first = imageInfoList.get(0);
            assertEquals("6f25adef061c3f2186c6910bff8cacd0c63e1493e3f8b616e52eb84076890bd1", first.getFingerprint());
            assertEquals("https://cloud-images.ubuntu.com/releases", first.getUpdateSource().getServer());
            assertEquals("ubuntu", first.getAliases().get(0).getName());
        }
    }

    @Test
    public void imageInfoTest() throws IOException, InterruptedException {
        try (TestHelper t = new TestHelper.Builder()
            .dispatchJsonFile("/1.0/images/aliases/ubuntu", "images_aliases_ubuntu.json")
            .dispatchJsonFile("/1.0/images/6f25adef061c3f2186c6910bff8cacd0c63e1493e3f8b616e52eb84076890bd1", "images_ubuntu.json")
            .build()) {

            ImageAliasesEntry imageAliasesEntry = t.client.imageGetAlias("ubuntu");
            ImageInfo imageInfo = t.client.imageInfo(imageAliasesEntry.getTarget());

            assertEquals(imageAliasesEntry.getTarget(), imageInfo.getFingerprint());
        }
    }
}
