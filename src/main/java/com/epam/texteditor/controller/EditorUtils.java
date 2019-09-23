package com.epam.texteditor.controller;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;

class EditorUtils {

    @SneakyThrows(IOException.class)
    static String getFileEncoding(File file) {
        UniversalDetector detector = new UniversalDetector(null);
        byte[] bytes = FileUtils.readFileToByteArray(file);
        detector.handleData(bytes, 0, bytes.length);
        detector.dataEnd();
        return detector.getDetectedCharset();
    }

    @SneakyThrows(IOException.class)
    static void makeReadOnly(File file) {
        // This method is required because file.setReadOnly() doesn't work for directory
        Path path = file.toPath();
        Files.setAttribute(path, "dos:readonly", true);
    }

    @SneakyThrows(IOException.class)
    static boolean isReadOnly(File file) {
        // This method is required because file.canWrite() always returns true for directory without SecurityManager
        Path path = file.toPath();
        DosFileAttributes dfa = Files.readAttributes(path, DosFileAttributes.class);
        return dfa.isReadOnly();
    }

    @SneakyThrows(IOException.class)
    static void updateOrCreateFile(File file, String changes) {
        // If file exists and writable or new
        if (!file.isDirectory() && file.canWrite() || !file.exists()) {
            String encoding = getFileEncoding(file);
            if (encoding == null) {
                encoding = "UTF-8";
            }

            FileUtils.writeStringToFile(file, changes, Charset.forName(encoding));
        }
    }

}
