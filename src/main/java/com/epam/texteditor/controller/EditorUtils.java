package com.epam.texteditor.controller;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.util.HashMap;
import java.util.Map;

@Component
class EditorUtils {

    private final ApplicationContext context;

    EditorUtils(ApplicationContext context) {
        this.context = context;
    }

    String extension(String fName) {
        int i = fName.lastIndexOf(".");
        return (i == -1) ? "default" : fName.substring(i);
    }

    boolean supported(String extension) {
        Map<String, String> supportedFiles = (HashMap<String, String>) context.getBean("supportedFilesAndIcons");
        return !"default".equals(extension) && supportedFiles.containsKey(extension);
    }

    String getFileEncoding(File file) {
        // Use before writing/reading
        UniversalDetector detector = new UniversalDetector(null);
        try {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            detector.handleData(bytes, 0, bytes.length);
            detector.dataEnd();
        } catch (IOException e) {
            System.out.println("Apache Commons IOException occurred!");
        }
        return detector.getDetectedCharset();
    }

    @SneakyThrows(IOException.class)
    void makeReadOnly(File file) {
        // This method is required because java.io.File.setReadOnly() doesn't work for directory
        Path path = file.toPath();
        Files.setAttribute(path, "dos:readonly", true);
    }

    @SneakyThrows(IOException.class)
    boolean isReadOnly(File file) {
        // This method is required because java.io.File.canWrite() always returns true for directory
        Path path = file.toPath();
        DosFileAttributes dfa = Files.readAttributes(path, DosFileAttributes.class);
        return dfa.isReadOnly();
    }

    @SneakyThrows(IOException.class)
    void updateOrCreateFile(File file, String changes) {
        // If file exists and writable or new
        if (!file.isDirectory() && ( file.canWrite() || !file.exists() )) {
            String encoding = getFileEncoding(file);
            if (encoding == null) {
                encoding = "UTF-8";
            }

            FileUtils.writeStringToFile(file, changes, Charset.forName(encoding));
        }
    }

    String checkAndReadFile(File file) {
        String extension = extension(file.getName());

        if (!supported(extension)) {
            return "FILE EXTENSION IS NOT SUPPORTED BY EDITOR";
        }

        return readFile(file);
    }

    @SneakyThrows(IOException.class)
    String readFile(File file) {
        // Read considering encoding
        String encoding = this.getFileEncoding(file);
        if (encoding == null) {
            encoding = "UTF-8";
        }

        return FileUtils.readFileToString(file,  Charset.forName(encoding));
    }

}
