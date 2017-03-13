package com.cloudbees.lxd.client;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;

public class LxdClientTlsTestIT {
    @Test
    public void connectWithTLS() throws Exception {
        Config clientConfig = buildTlsConfig();
        try (LxdClient tlsClient = new LxdClient(clientConfig)) {
            Assert.assertEquals("trusted", tlsClient.server().blockingGet().getAuth());
        }
    }

    public static Config buildTlsConfig() throws IOException {
        return Config.remoteAccessConfig("https://localhost:8443",
            Config.readFile(Paths.get("vagrant/server.crt")),
            Config.readFile(Paths.get("vagrant/client.crt")),
            Config.readFile(Paths.get("vagrant/client.key")), null);
    }
}
