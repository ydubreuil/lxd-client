package com.cloudbees.lxd;

import org.junit.Test;

import java.io.IOException;

public class DefaultLXDClientTest {
    @Test
    public void miscOptions() throws IOException, InterruptedException {
        DefaultLXDClient client = new DefaultLXDClient(Config.localAccessConfig("/home/dudu/CloudBees/projects/lxd-mansion/src/lxd-java-client/lxd.socket"));
        System.out.println(client.containers());
        System.out.println(client.serverConfiguration());
    }
}
