package com.epam.texteditor.controller;

import com.epam.texteditor.model.Note;
import com.epam.texteditor.service.NoteService;
import org.apache.commons.io.FileUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;


@Controller
public class MainController {

    private final ConfigurableApplicationContext context;
    private final NoteService noteService;

    // Synchronizes GET file and POST note requests
    private String curFile;

    public MainController(ConfigurableApplicationContext context, NoteService noteService) {
        this.context = context;
        this.noteService = noteService;
    }

    private void setDocAndNotes(Model model) {
        File root = (File) context.getBean("root");

        // Set text
        File newTempFile = FileUtils.getFile(root, curFile);
        try {
            String text = FileUtils.readFileToString(newTempFile, Charset.defaultCharset());
            model.addAttribute("text", text);
        } catch (FileNotFoundException e) {
            model.addAttribute("text", "File not found!");
        } catch (IOException e) {
            model.addAttribute("text", "IOException occurred!");
        }

        // Set notes
        model.addAttribute("notes", noteService.getNotesByFilename(curFile));
    }

    @GetMapping("/")
    public String indexGet(Model model, @RequestParam(value="file_name", required=false) String fileName) {
        // Default file if it's not specified
        curFile = (fileName == null) ? "readme.txt" : fileName;

        setDocAndNotes(model);

        return "index";
    }

    @PostMapping("/")
    public String indexPost(Model model, @RequestParam(value="note_name") String noteName,
                                                @RequestParam(value="note_text") String noteText) {

        noteService.saveNote(new Note(noteName, noteText, curFile));

        setDocAndNotes(model);

        return "index";
    }

}
