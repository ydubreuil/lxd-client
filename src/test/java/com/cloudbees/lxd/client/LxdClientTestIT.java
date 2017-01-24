package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.ContainerPut;
import com.cloudbees.lxd.client.api.Network;
import com.cloudbees.lxd.client.api.builder.NetworkConfigBuilder;
import com.cloudbees.lxd.client.api.Server;
import com.cloudbees.lxd.client.api.StatusCode;
import io.reactivex.Completable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Tests using the vagrant box
 */
public class LxdClientTestIT {

    LxdClient client;

    @Before
    public void setUp() throws IOException {
        Config tlsConfig = LxdClientTlsTestIT.buildTlsConfig();
        Config unixSocketConfig = Config.localAccessConfig(Paths.get("lxd.socket").toAbsolutePath().toString());

        Config usedConfig = Boolean.getBoolean("lxd.unixsocket") ? unixSocketConfig : tlsConfig;
        client = new LxdClient(usedConfig);
    }

    /**
     * Checks that the connection to LXD is trusted. This is required for other tests to work
     * @throws Exception
     */
    @Test
    public void connectionTrusted() throws Exception {
        Server server = client.server().blockingGet();
        Assert.assertEquals("trusted", server.getAuth());
    }

    @Test
    public void containerSimpleLifecycle() throws InterruptedException {
        Assert.assertEquals("trusted", client.server().blockingGet().getAuth());
        LxdClient.ContainerClient container = client.container("it-" + Long.toHexString(System.nanoTime()));

        try {
            container.init("ubuntu", "16.04", new ContainerPut()).doOnComplete(() -> System.out.println("Container created"))
                .andThen(container.start().doOnComplete(() -> System.out.println("Container started")))
                .andThen(container.filePush("/home/ubuntu/.ssh/authorized_keys", 1000, 1000, "400", RequestBody.create(MediaType.parse("application/octet-stream"), "ssh-rsa this is a joke!"))
                    .compose(t -> LxdClientHelpers.retryOnFailure(t, 10))
                    .doOnComplete(() -> System.out.println("SSH key pushed")))
                    .doOnError(throwable -> {throw new AssertionError(throwable);})
                .blockingAwait();

            Assert.assertEquals(StatusCode.Running, container.state().blockingGet().getStatusCode());

            ByteArrayOutputStream stdout = new ByteArrayOutputStream();
            Integer exitCodeCat = container.execute(Arrays.asList("/bin/cat", "/home/ubuntu/.ssh/authorized_keys"), null, null, stdout, null).blockingGet();
            Assert.assertEquals(0, exitCodeCat.intValue());
            Assert.assertEquals("ssh-rsa this is a joke!", new String(stdout.toByteArray()));

            container.stop(0, false, false).blockingAwait();
            Assert.assertEquals(StatusCode.Stopped, container.state().blockingGet().getStatusCode());
            container.start().blockingAwait();
            Assert.assertEquals(StatusCode.Running, container.state().blockingGet().getStatusCode());

            Integer exitCode1 = container.execute(Arrays.asList("/bin/sh", "-c", "exit 7"), null, null, null, null).blockingGet();
            Assert.assertEquals(7, exitCode1.intValue());
        } finally {
            System.out.println("container.stop");
            container.stop(0, false, false).blockingAwait();

            System.out.println("container.delete");
            container.delete().blockingAwait();
        }
    }

    @Test
    public void networkSimpleLifecycle() throws InterruptedException {
        Assert.assertEquals("trusted", client.server().blockingGet().getAuth());

        LxdClient.NetworkClient networkClient = client.network("br-" + Long.toHexString(System.nanoTime()));
        try {
            Completable networkCreation = networkClient.create(
                new NetworkConfigBuilder()
                    .dnsMode(NetworkConfigBuilder.DnsMode.MANAGED)
                    .ipv4Address("10.38.189.1/24")
                    .ipv6Address(null)
                    .build());

            Network network = networkCreation.andThen(networkClient.info()).blockingGet();
            Assert.assertEquals(networkClient.networkName, network.getName());
            Assert.assertEquals("bridge", network.getType());
            Assert.assertEquals("10.38.189.1/24", network.getConfig().get("ipv4.address"));
            Assert.assertEquals("none", network.getConfig().get("ipv6.address"));
            Assert.assertEquals("managed", network.getConfig().get("dns.mode"));

            Completable networkUpdate = networkClient.update(
                new NetworkConfigBuilder()
                    .dnsMode(NetworkConfigBuilder.DnsMode.NONE)
                    .build());

            network = networkUpdate.andThen(networkClient.info()).blockingGet();
            Assert.assertEquals("none", network.getConfig().get("dns.mode"));

            List<Network> networks = client.networks().blockingGet();
            for (Network n : networks) {
                if (networkClient.networkName.equals(n.getName())) {
                    return;
                }
            }

            fail();
        } finally {
            networkClient.delete()
                .doOnComplete(() -> System.out.println("deleted"))
                .blockingAwait();
        }
    }
}
