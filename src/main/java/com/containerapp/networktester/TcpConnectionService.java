package com.containerapp.networktester;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

@Service
public class TcpConnectionService {

    public String checkTcpConnection(String fqdn, int port) {
        String dnsInfo = getDnsInfo(fqdn);
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(fqdn, port), 5000);
            return "Connection is alive. " + dnsInfo;
        } catch (IOException e) {
            return "Connection is dead. " + dnsInfo + "\nError: " + e.getMessage();
        }
    }

    private String getDnsInfo(String fqdn) {
        String dnsServer = getDnsServer(fqdn);
        try {
            InetAddress inetAddress = InetAddress.getByName(fqdn);
            String ipAddress = inetAddress.getHostAddress();
            return "Resolved IP: " + ipAddress + "\nDNS Server: " + dnsServer;
        } catch (UnknownHostException e) {
            return "DNS resolution failed. DNS Server: " + dnsServer + "\nError: " + e.getMessage();
        }
    }

    private String getDnsServer(String fqdn) {
        String dnsServer = "Unknown";
        try {
            Process process = Runtime.getRuntime().exec("dig " + fqdn);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("SERVER")) {
                	String tempLine[] = line.split(":");
                	if(tempLine.length>1) {
                        dnsServer = tempLine[1].trim();
                        break;
                	}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dnsServer;
    }
}