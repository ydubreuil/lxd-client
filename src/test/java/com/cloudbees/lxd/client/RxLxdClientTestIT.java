package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.ImageAliasesEntry;
import com.cloudbees.lxd.client.api.ImageInfo;
import com.cloudbees.lxd.client.api.Operation;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RxLxdClientTestIT {

    RxLxdClient client;

    @Before
    public void setUp() {
        client = new RxLxdClient(Config.localAccessConfig(Paths.get("lxd.socket").toAbsolutePath().toString()));
    }

    @Test
    public void listImages()  {
        List<ImageInfo> imageInfos = client.images().blockingGet();
        ImageAliasesEntry aliasesEntry = client.alias("ubuntu").blockingGet();
        ImageInfo imageInfo = client.image(aliasesEntry.getTarget()).info().blockingGet();
    }

    @Test
    public void containerSimpleLifecycle() throws InterruptedException {
        RxLxdClient.Container container = client.container("it-" + Long.toHexString(System.nanoTime()));

        System.out.println("container.init");
        Operation creation = container.init("ubuntu", "16.04", null, null, null, true).blockingGet();
        assertEquals(Operation.Status.Success, creation.getStatusCode());

        System.out.println("container.start");
        Operation start = container.start(0, false, false).blockingGet();
        assertEquals(Operation.Status.Success, start.getStatusCode());

        Thread.sleep(1000 * 5);

        System.out.println("filePush");
        container.filePush("/home/ubuntu/.ssh/authorized_keys", 1000, 1000, "400", RequestBody.create(MediaType.parse("application/octet-stream"), "ssh-rsa this is a joke!"))
            .blockingAwait();

        Operation stop = container.stop(0, false, false).blockingGet();
        assertEquals(Operation.Status.Success, stop.getStatusCode());

        container.delete().blockingAwait();
    }
}
