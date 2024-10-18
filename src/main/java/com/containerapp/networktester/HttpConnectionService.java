package com.containerapp.networktester;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class HttpConnectionService {

    private final WebClient webClient;

    public HttpConnectionService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<String> checkHttpConnection(String url) {
        String ipAddress = resolveIpAddress(url);

        return webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(String.class)
                .map(response -> {
                    boolean isAlive = response.getStatusCode().is2xxSuccessful();
                    String httpResponse = "HTTP Status: " + response.getStatusCode() + "\nResponse Body: " + response.getBody();
                    String ipInfo = ipAddress != null ? "Resolved IP: " + ipAddress : "IP resolution failed.";
                    return isAlive ? "Connection is alive. " + ipInfo + "\n" + httpResponse : "Connection is dead. " + ipInfo + "\n" + httpResponse;
                })
                .onErrorResume(e -> Mono.just("Connection failed. " + (ipAddress != null ? "Resolved IP: " + ipAddress : "IP resolution failed.") 
                        + "\nError: " + getStackTraceAsString(e)));
    }

    private String resolveIpAddress(String url) {
        try {
            InetAddress inetAddress = InetAddress.getByName(new java.net.URL(url).getHost());
            return inetAddress.getHostAddress();
        } catch (UnknownHostException | java.net.MalformedURLException e) {
            return null;
        }
    }

    private String getStackTraceAsString(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
