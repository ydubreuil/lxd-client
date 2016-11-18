package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.LxdResponse;
import com.cloudbees.lxd.client.api.Operation;
import com.cloudbees.lxd.client.api.ImageAliasesEntry;
import com.cloudbees.lxd.client.api.ImageInfo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

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
    public void createContainer() {
        final String name = "it-" + Long.toHexString(System.nanoTime());
        LxdResponse<Operation> container = client.containerInit(name, "ubuntu", "16.04", null, null, null, true);
        System.out.println(ToStringBuilder.reflectionToString(container));
    }
}
