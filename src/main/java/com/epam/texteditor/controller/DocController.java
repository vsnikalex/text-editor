package com.epam.texteditor.controller;

import com.epam.texteditor.model.Doc;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.io.File;


@Controller
public class DocController {

    @MessageMapping("/text")
    @SendTo("/topic/files")
    public Doc doc(Doc doc) {

        // Save to file system
        EditorUtils.updateOrCreateFile(new File(doc.getFile()), doc.getText());

        return doc;
    }

}
