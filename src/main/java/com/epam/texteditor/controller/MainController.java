package com.epam.texteditor.controller;

import com.epam.texteditor.model.Note;
import com.epam.texteditor.service.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
public class MainController {

    private final NoteService noteService;

    public MainController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/")
    public String indexGet(Model model) {
        model.addAttribute("nodes", noteService.getAllNotes());

        return "index";
    }

    @PostMapping("/")
    public String indexPost(Model model, HttpServletRequest request) {
        noteService.saveNote(new Note(request.getParameter("node_name"), request.getParameter("node_text")));

        model.addAttribute("nodes", noteService.getAllNotes());

        return "index";
    }

}
