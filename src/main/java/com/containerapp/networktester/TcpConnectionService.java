package com.containerapp.networktester;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

@Service
public class TcpConnectionService {

    public String checkTcpConnection(String fqdn, int port) {
        String ipAddress = "Unknown IP";
        
        try {
            // Resolve IP address before attempting the connection
            InetAddress inetAddress = InetAddress.getByName(fqdn);
            ipAddress = inetAddress.getHostAddress();
            
            try (Socket socket = new Socket()) {
                // When using InetSocketAddress in Java, you can provide either an IP address or a fully qualified domain name (FQDN)
                // So the use could provide the FQDN or IP address, and the code will work in both cases
                socket.connect(new InetSocketAddress(fqdn, port), 5000);
                return "Connection is alive. Sent to IP: " + ipAddress;
            }
        } catch (UnknownHostException e) {
            return "DNS resolution failed for " + fqdn + ".\nError: " + getStackTraceAsString(e);
        } catch (IOException e) {
            return "Connection is dead. Attempted to connect to IP: " + ipAddress + ".\nError: " + getStackTraceAsString(e);
        }
    }

    private String getStackTraceAsString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
