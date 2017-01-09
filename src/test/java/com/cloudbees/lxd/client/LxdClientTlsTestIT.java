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
            Assert.assertEquals("trusted", tlsClient.serverState().blockingGet().getAuth());
        }
    }

    public static Config buildTlsConfig() throws IOException {
        return Config.remoteAccessConfig("https://localhost:8443", readFile("vagrant/server.crt"), readFile("vagrant/client.crt"), readFile("vagrant/client.key"), null);
    }

    private static String readFile(String name) throws IOException {
        return new okio.Buffer().readFrom(new FileInputStream(Paths.get(name).toFile())).readString(Charset.defaultCharset());
    }
}
