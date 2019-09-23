package com.epam.texteditor.controller;

import com.epam.texteditor.model.Note;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NoteController {

    @MessageMapping("/note")
    @SendTo("/topic/notes")
    public Note note(Note note) throws Exception {

        // TODO: save to DB
        Thread.sleep(1000); // simulated delay

        return note;
    }

}
