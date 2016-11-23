package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.ContainerAction;
import com.cloudbees.lxd.client.api.ContainerInfo;
import com.cloudbees.lxd.client.api.ContainerState;
import com.cloudbees.lxd.client.api.LxdResponse;
import com.cloudbees.lxd.client.api.Operation;
import com.cloudbees.lxd.client.api.ImageAliasesEntry;
import com.cloudbees.lxd.client.api.ImageInfo;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DefaultLXDClientTestIT {

    DefaultLXDClient client;

    @Before
    public void setUp() {
        client = new DefaultLXDClient(Config.localAccessConfig(Paths.get("lxd.socket").toAbsolutePath().toString()));
    }

    @Test
    public void miscOptions() {
        System.out.println(client.listContainers().stream()
            .map(containerInfo -> ToStringBuilder.reflectionToString(containerInfo, ToStringStyle.MULTI_LINE_STYLE))
            .reduce("", String::concat));
        System.out.println(ToStringBuilder.reflectionToString(client.serverStatus(), ToStringStyle.MULTI_LINE_STYLE));
    }

    @Test
    public void listImages()  {
        List<ImageInfo> imageInfos = client.listImages();
        ImageAliasesEntry aliasesEntry = client.imageGetAlias("ubuntu");
        ImageInfo imageInfo = client.imageInfo(aliasesEntry.getTarget());
    }

    @Test
    public void containerSimpleLifecycle() throws InterruptedException {
        final String name = "it-" + Long.toHexString(System.nanoTime());
        LxdResponse<Operation> containerCreation = client.containerInit(name, "ubuntu", "16.04", null, null, null, true);
        LxdResponse<Operation> containerCreated = client.waitForCompletion(containerCreation);
        assertEquals(Operation.Status.Success.getValue(), containerCreated.getStatusCode().intValue());

        LxdResponse<Operation> containerStart = client.containerAction(name, ContainerAction.Start, 0, false, false);
        LxdResponse<Operation> containerStarted = client.waitForCompletion(containerStart);
        assertEquals(Operation.Status.Success.getValue(), containerStarted.getStatusCode().intValue());

        Thread.sleep(1000 * 5);
        client.filePush(name, "/home/ubuntu/.ssh/authorized_keys", 1000, 1000, "400", RequestBody.create(MediaType.parse("application/octet-stream"), "ssh-rsa this is a joke!"));

        Thread.sleep(1000 * 500);

        LxdResponse<Operation> containerStop = client.containerAction(name, ContainerAction.Stop, 0, false, false);
        LxdResponse<Operation> containerStopped = client.waitForCompletion(containerStop);
        assertEquals(Operation.Status.Success.getValue(), containerStopped.getStatusCode().intValue());

        LxdResponse<Operation> containerDelete = client.containerDelete(name);
        LxdResponse<Operation> containerDeleted = client.waitForCompletion(containerDelete);
        assertEquals(Operation.Status.Success.getValue(), containerDeleted.getStatusCode().intValue());
    }

    @Test
    public void filePush() {
        final String name = "it-manual";
        client.filePush(name, "/pom.xml", 0, 0, "777", Paths.get("pom.xml").toFile());
    }

    @Test
    public void containerInfo() {
        final String name = "it-manual";
        ContainerState containerState = client.containerState(name);
        System.out.println(ToStringBuilder.reflectionToString(containerState));

    }
}
