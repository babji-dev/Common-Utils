package com.commonUtils.utils;

import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

@Component
public class NetworkTester {

    public String getPrimaryIp() throws Exception {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface netInterface = interfaces.nextElement();
            if (!netInterface.isUp() || netInterface.isLoopback() || netInterface.isVirtual()) continue;

            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (!addr.isLoopbackAddress() && addr.getHostAddress().indexOf(':') == -1) {
                    return addr.getHostAddress(); // return first non-loopback IPv4
                }
            }
        }
        return "Unknown IP";
    }

}
