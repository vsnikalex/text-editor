package com.epam.texteditor.controller;

import com.epam.texteditor.model.Doc;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class DocController {

    @MessageMapping("/text")
    @SendTo("/topic/files")
    public Doc doc(Doc doc) throws Exception {

        // Save to file system
        EditorUtils.updateOrCreateFile(doc.getFile(), doc.getText());

        return doc;
    }

}
