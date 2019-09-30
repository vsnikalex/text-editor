package com.epam.texteditor.controller;

import com.epam.texteditor.model.Doc;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@Controller
public class DocController {

    private final EditorUtils editorUtils;

    public DocController(EditorUtils editorUtils) {
        this.editorUtils = editorUtils;
    }

    // Editor
    @MessageMapping("/text/{fileHash}")
    @SendTo("/topic/files/{fileHash}")
    public Doc doc(Doc doc, @DestinationVariable String fileHash) {
        // Save to file system
        editorUtils.updateOrCreateFile(new File(doc.getFile()), doc.getText());
        // Send changes to listeners
        return doc;
    }

    // Previews
    @MessageMapping("/text")
    @SendTo("/topic/files")
    public String text(String filePath) {
        return editorUtils.checkCompatibilityAndRead(new File(filePath));
    }

    // Download
    @SneakyThrows(IOException.class)
    @GetMapping(value = "/download")
    public void getFile(HttpSession session, HttpServletResponse response) {
        File curFile = (File)session.getAttribute("curFile");

        if (!curFile.exists()) {
            response.sendRedirect("/");
            return;
        }

        String filePath = curFile.getAbsolutePath();

        InputStream in = new FileInputStream(filePath);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-disposition", "attachment; filename="+ curFile.getName());
        IOUtils.copy(in, response.getOutputStream());
    }

}
