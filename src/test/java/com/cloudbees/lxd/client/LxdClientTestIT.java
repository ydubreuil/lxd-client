package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.ImageAliasesEntry;
import com.cloudbees.lxd.client.api.ImageInfo;
import com.cloudbees.lxd.client.api.Operation;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class LxdClientTestIT {

    LxdClient client;

    @Before
    public void setUp() {
        client = new LxdClient(Config.localAccessConfig(Paths.get("lxd.socket").toAbsolutePath().toString()));
    }

    @Test
    public void listImages()  {
        List<ImageInfo> imageInfos = client.images().blockingGet();
        ImageAliasesEntry aliasesEntry = client.alias("ubuntu").blockingGet();
        ImageInfo imageInfo = client.image(aliasesEntry.getTarget()).info().blockingGet();
    }

    @Test
    public void containerSimpleLifecycle() throws InterruptedException {
        LxdClient.Container container = client.container("it-" + Long.toHexString(System.nanoTime()));

        try {
            container.init("ubuntu", "16.04", null, null, null, true).doOnComplete(() -> System.out.println("Container created"))
                .andThen(container.start().doOnComplete(() -> System.out.println("Container started")))
                .andThen(container.filePush("/home/ubuntu/.ssh/authorized_keys", 1000, 1000, "400", RequestBody.create(MediaType.parse("application/octet-stream"), "ssh-rsa this is a joke!"))
                    .compose(t -> LxdClientHelpers.retryOnFailure(t, 10))
                    .doOnComplete(() -> System.out.println("SSH key pushed")))
                .blockingAwait();
        } finally {
            System.out.println("container.stop");
            container.stop(0, false, false).blockingAwait();

            System.out.println("container.delete");
            container.delete().blockingAwait();
        }
    }
}
