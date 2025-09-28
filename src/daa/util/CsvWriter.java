package daa.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class CsvWriter {
    private final File file;
    private final String[] header;

    public CsvWriter(File file, String... header) {
        this.file = file;
        this.header = header;
    }

    public synchronized void appendRow(String... values) throws IOException {
        boolean writeHeader = ensureParentAndHeader();
        try (BufferedWriter w = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, true))) {
            if (writeHeader) {
                w.write(String.join(",", escapeCsv(header)));
                w.newLine();
            }
            w.write(String.join(",", escapeCsv(values)));
            w.newLine();
        }
    }

    private boolean ensureParentAndHeader() throws IOException {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        if (!file.exists()) {
            Files.createFile(file.toPath());
            return true;
        }
        // If empty, write header
        return Files.size(file.toPath()) == 0L;
    }

    private static List<String> escapeCsv(String[] arr) {
        return Arrays.stream(arr).map(CsvWriter::escape).toList();
    }

    private static String escape(String s) {
        if (s == null) return "";
        boolean needsQuotes = s.contains(",") || s.contains("\n") || s.contains("\"");
        String t = s.replace("\"", "\"\"");
        return needsQuotes ? "\"" + t + "\"" : t;
    }
}
