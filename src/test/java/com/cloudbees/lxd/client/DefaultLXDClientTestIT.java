package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.AsyncOperation;
import com.cloudbees.lxd.client.api.ImageAliasesEntry;
import com.cloudbees.lxd.client.api.ImageInfo;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

public class DefaultLXDClientTestIT {

    DefaultLXDClient client;

    @Before
    public void setUp() {
        client = new DefaultLXDClient(Config.localAccessConfig(Paths.get("lxd.socket").toAbsolutePath().toString()));
    }

    @Ignore
    @Test
    public void miscOptions() {
        System.out.println(client.listContainers());
        System.out.println(client.serverStatus());
    }

    @Test
    public void listImages()  {
        List<ImageInfo> imageInfos = client.listImages();
        System.out.println(imageInfos);
        ImageAliasesEntry aliasesEntry = client.imageGetAlias("ubuntu:16.04");
        ImageInfo imageInfo = client.imageInfo("ubuntu:16.04");
    }

    @Test
    public void createContainer()  {
        AsyncOperation container = client.containerInit("nico-canard", "ubuntu", "16.04", null, null, null, true);
        System.out.println(container);
    }
}
