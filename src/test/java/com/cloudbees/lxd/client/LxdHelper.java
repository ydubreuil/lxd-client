package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.ContainerState;
import com.cloudbees.lxd.client.api.ContainerStateNetwork;
import com.cloudbees.lxd.client.api.ContainerStateNetworkAddress;

public class LxdHelper {
    public static String getFirstIPv4Address(ContainerState state) {
        String firstIpV4Address = "";
        for (ContainerStateNetwork network : state.getNetwork().values()) {
            if (network.getType().equals("loopback")) {
                continue;
            }
            for (ContainerStateNetworkAddress address : network.getAddresses()) {
                if (address.getScope().contains("link") || address.getScope().contains("local")) {
                    continue;
                }
                if (address.getFamily().equals("inet")) {
                    firstIpV4Address = address.getAddress();
                }

                break;
            }
        }
        return firstIpV4Address;
    }
}
