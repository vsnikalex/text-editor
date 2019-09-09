package com.epam.texteditor.controller;

import com.epam.texteditor.model.Note;
import com.epam.texteditor.service.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class MainController {

    private final NoteService noteService;

    // Default file
    private String curFile = "/readme.txt";

    public MainController(NoteService noteService) {
        this.noteService = noteService;

        // Default file's notes
        noteService.saveNote(new Note("Note1", "Note1 text", curFile));
        noteService.saveNote(new Note("Note2", "Note2 text", curFile));
        noteService.saveNote(new Note("Note3", "Note3 text", curFile));
    }

    @GetMapping("/")
    public String indexGet(Model model, @RequestParam(value="file_name", required=false) String fileName) {

        curFile = fileName;

        model.addAttribute("fileName", curFile);
        model.addAttribute("notes", noteService.getNotesByFilename(curFile));

        return "index";
    }

    @PostMapping("/")
    public String indexPost(Model model, @RequestParam(value="note_name") String noteName,
                                                @RequestParam(value="note_text") String noteText) {

        noteService.saveNote(new Note(noteName, noteText, curFile));

        model.addAttribute("fileName", curFile);
        model.addAttribute("notes", noteService.getNotesByFilename(curFile));

        return "index";
    }

}
