package com.containerapp.networktester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TcpDumpController {

    private static final String DUMP_DIR = "/app/dumps/";

    @Autowired
    private TcpDumpService tcpDumpService;

    @GetMapping("/test-tcpdump")
    public String showTcpDumpForm() {
        return "tcpDumpForm";
    }

    @PostMapping("/run-tcpdump")
    public String runTcpDump(@RequestParam int duration, Model model) {
        if (duration < 30 || duration > 600) {
            model.addAttribute("error", "Duration must be between 30 and 6000 seconds.");
            return "tcpDumpForm";
        }
        String fileName = tcpDumpService.runTcpDump(duration);
        model.addAttribute("fileName", fileName);
        return "redirect:/download-tcpdump";
    }

    @GetMapping("/download-tcpdump")
    public String showDownloadPage(Model model) {
        List<String> files = tcpDumpService.listDumpFiles();
        model.addAttribute("files", files);
        return "downloadPage";
    }

    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> downloadFile(@RequestParam String fileName) throws IOException {
        File file = new File(DUMP_DIR + fileName);
        StreamingResponseBody responseBody = outputStream -> {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.tcpdump.pcap")
                .body(responseBody);
    }

    @PostMapping("/delete")
    public String deleteFile(@RequestParam String fileName) throws IOException {
        Files.deleteIfExists(Paths.get(DUMP_DIR + fileName));
        return "redirect:/download-tcpdump";
    }
}
