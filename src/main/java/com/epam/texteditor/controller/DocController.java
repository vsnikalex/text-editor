package com.epam.texteditor.controller;

import com.epam.texteditor.model.Doc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.io.File;


@Controller
public class DocController {

    private final EditorUtils editorUtils;

    public DocController(EditorUtils editorUtils) {
        this.editorUtils = editorUtils;
    }

    // For editor
    @MessageMapping("/text/{fileHash}")
    @SendTo("/topic/files/{fileHash}")
    public Doc doc(Doc doc, @DestinationVariable String fileHash) {
        // Save to file system
        editorUtils.updateOrCreateFile(new File(doc.getFile()), doc.getText());
        // Send changes to listeners
        return doc;
    }

    // For previews
    @MessageMapping("/text")
    @SendTo("/topic/files")
    public String text(String filePath) {
        return editorUtils.readFile(new File(filePath));
    }

}
