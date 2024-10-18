package com.containerapp.networktester;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TcpDumpService {

    private static final String DUMP_DIR = "/app/dumps/";

    public String runTcpDump(int duration) {
        // Ensure the dumps directory exists
        File dumpDir = new File(DUMP_DIR);
        if (!dumpDir.exists()) {
            dumpDir.mkdirs();
        }

        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "tcpdump_" + timestamp + ".pcap";
        String command = String.format("tcpdump -w %s%s -G %d -W 1", DUMP_DIR, fileName, duration);

        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return fileName;
    }

    public List<String> listDumpFiles() {
        try {
            return Files.list(Paths.get(DUMP_DIR))
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}