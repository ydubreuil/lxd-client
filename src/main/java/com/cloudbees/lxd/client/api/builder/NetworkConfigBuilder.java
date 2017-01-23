package com.cloudbees.lxd.client.api.builder;

import java.util.HashMap;

/**
 * Utility class to configure an LXD network
 */
public class NetworkConfigBuilder {
    public enum DnsMode {
        /**
         * will have one DNS record per container, matching its name and known IP addresses. The container cannot alter this record through DHCP
         */
        MANAGED("managed"),

        /**
         * allows the containers to self-register in the DNS through DHCP. So whatever hostname the container sends during the DHCP negotiation ends up in DNS.
         */
        DYNAMIC("dynamic"),

        /**
         * simple recursive DNS server without any kind of local DNS records
         */
        NONE("none");

        private final String value;
        DnsMode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    protected HashMap<String, String> config = new HashMap<>();

    /**
     * @param ipv4Address CIDR of the network
     * @return
     */
    public NetworkConfigBuilder ipv4Address(String ipv4Address) {
        config.put("ipv4.address", ipv4Address != null ? ipv4Address : "none");

        return this;
    }

    public NetworkConfigBuilder ipv4Nat(Boolean enabled) {
        config.put("ipv4.nat", enabled != null ? enabled.toString() : "false");

        return this;
    }

    public NetworkConfigBuilder ipv6Address(String ipv6Address) {
        config.put("ipv6.address", ipv6Address != null ? ipv6Address : "none");

        return this;
    }

    public NetworkConfigBuilder ipv6Nat(Boolean enabled) {
        config.put("ipv6.nat", enabled != null ? enabled.toString() : "false");

        return this;
    }

    public NetworkConfigBuilder bridgeDriver(String driver) {
        config.put("bridge.driver", driver);

        return this;
    }


    public NetworkConfigBuilder dnsDomain(String domain) {
        config.put("dns.domain", domain);

        return this;
    }

    public NetworkConfigBuilder dnsMode(DnsMode mode) {
        config.put("dns.mode", mode.getValue());

        return this;
    }

    public HashMap<String, String> build() {
        return config;
    }
}
