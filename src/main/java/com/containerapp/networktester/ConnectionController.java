package com.containerapp.networktester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ConnectionController {
    @Autowired
    private HttpConnectionService httpConnectionService;

    @Autowired
    private TcpConnectionService tcpConnectionService;

    @Autowired
    private DigService digService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/test-https-connection")
    public String showHttpsForm() {
        return "httpsForm";
    }

    @GetMapping("/check-https-connection")
    public String checkHttpsConnection(@RequestParam String url, Model model) {
        if (!url.startsWith("https://")) {
            url = "https://" + url;
        }
        String result = httpConnectionService.checkHttpConnection(url).block();
        model.addAttribute("result", result);
        model.addAttribute("inputUrl", url);
        return "result";
    }

    @GetMapping("/test-tcp-connection")
    public String showTcpForm() {
        return "tcpForm";
    }

    @GetMapping("/check-tcp-connection")
    public String checkTcpConnection(@RequestParam String fqdn, @RequestParam int port, Model model) {
        String result = tcpConnectionService.checkTcpConnection(fqdn, port);
        model.addAttribute("result", result);
        model.addAttribute("inputFqdn", fqdn);
        model.addAttribute("inputPort", port);
        return "tcpResult";
    }

    @GetMapping("/test-dig")
    public String showDigForm() {
        return "digForm";
    }

    @GetMapping("/check-dig")
    public String checkDig(@RequestParam String fqdn, @RequestParam(required = false) String dnsServer, Model model) {
        String command = "dig " + fqdn;
        if (dnsServer != null && !dnsServer.isEmpty()) {
            command += " @" + dnsServer;
        }
        String result = digService.runDig(fqdn, dnsServer);
        model.addAttribute("result", result);
        model.addAttribute("inputFqdn", fqdn);
        model.addAttribute("command", command); // Add this line
        return "digResult";
    }
}