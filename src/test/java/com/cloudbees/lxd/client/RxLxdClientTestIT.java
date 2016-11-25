package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.ImageAliasesEntry;
import com.cloudbees.lxd.client.api.ImageInfo;
import com.cloudbees.lxd.client.api.Operation;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

        try {
            System.out.println("container.init");
            Operation creation = container.init("ubuntu", "16.04", null, null, null, true).blockingGet();
            assertEquals(Operation.Status.Success, creation.getStatusCode());

            System.out.println("container.start");
            Operation start = container.start(0, false, false).blockingGet();
            assertEquals(Operation.Status.Success, start.getStatusCode());

            System.out.println("container.filePush");
            container.filePush("/home/ubuntu/.ssh/authorized_keys", 1000, 1000, "400", RequestBody.create(MediaType.parse("application/octet-stream"), "ssh-rsa this is a joke!"))
                .toObservable()
                .retryWhen(errors ->
                    errors
                    .zipWith(Observable.range(1, 10), (n, i) -> i)
                    .flatMap(retryCount -> Observable.timer((long) retryCount, TimeUnit.SECONDS))
            ).blockingSubscribe();
        } finally {
            System.out.println("container.stop");
            Operation stop = container.stop(0, false, false).blockingGet();
            assertEquals(Operation.Status.Success, stop.getStatusCode());

            System.out.println("container.delete");
            container.delete().blockingAwait();
        }
    }
}
