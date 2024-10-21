package com.containerapp.networktester;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class DigService {
    public String runDig(String fqdn, String dnsServer) {
        String command = "dig " + fqdn;
        if (dnsServer != null && !dnsServer.isEmpty()) {
            command += " @" + dnsServer;
        }

        StringBuilder result = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }

        return result.toString();
    }
}