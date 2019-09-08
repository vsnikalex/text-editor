package com.epam.texteditor.controller;

import com.epam.texteditor.model.Note;
import com.epam.texteditor.service.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    private final NoteService noteService;

    public MainController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        // Mocked Nodes
        try {
            noteService.saveNote(new Note("Node1", "first node text describing the main problem that is necessary to solve"));
            Thread.sleep(10);
            noteService.saveNote(new Note("Node2", "second node text describing the main problem that is necessary to solve"));
            Thread.sleep(10);
            noteService.saveNote(new Note("Node3", "third node text describing the main problem that is necessary to solve"));
            Thread.sleep(10);
            noteService.saveNote(new Note("Node4", "fourth node text describing the main problem that is necessary to solve"));
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

        String pageTitle = "EPAM Text Editor";
        String appName = "Text Editor Home Page";

        model.addAttribute("appName", appName);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("nodes", noteService.getAllNotes());

        return "index";
    }

}
