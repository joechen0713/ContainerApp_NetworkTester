package com.containerapp.networktester;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class HttpConnectionService {

    private final WebClient webClient;

    public HttpConnectionService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<String> checkHttpConnection(String url) {
        String dnsInfo = getDnsInfo(url);
        return webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(String.class)
                .map(response -> {
                    boolean isAlive = response.getStatusCode().is2xxSuccessful();
                    String httpResponse = "HTTP Status: " + response.getStatusCode() + "\nResponse Body: " + response.getBody();
                    return isAlive ? "Connection is alive. " + dnsInfo + "\n" + httpResponse : "Connection is dead. " + dnsInfo + "\n" + httpResponse;
                })
                .onErrorResume(e -> Mono.just("Connection failed. " + dnsInfo + "\nError: " + e.getMessage()));
    }

    private String getDnsInfo(String url) {
        String dnsServer = getDnsServer(url);
        try {
            InetAddress inetAddress = InetAddress.getByName(new java.net.URL(url).getHost());
            String ipAddress = inetAddress.getHostAddress();
            return "Resolved IP: " + ipAddress + "\nDNS Server: " + dnsServer;
        } catch (UnknownHostException | java.net.MalformedURLException e) {
            return "DNS resolution failed. DNS Server: " + dnsServer + "\nError: " + e.getMessage();
        }
    }

    private String getDnsServer(String url) {
        String dnsServer = "Unknown";
        try {
            String host = new java.net.URL(url).getHost();
            Process process = Runtime.getRuntime().exec("dig " + host);
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