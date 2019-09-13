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
    private final File curDir;

    public MainController(ConfigurableApplicationContext context, NoteService noteService) {
        this.context = context;
        this.noteService = noteService;
        curDir = (File) context.getBean("root");
    }

    private void setDocAndNotes(Model model) {
        // Set text
        File newTempFile = FileUtils.getFile(curDir, curFile);
        try {
            String text = FileUtils.readFileToString(newTempFile, Charset.defaultCharset());
            model.addAttribute("text", text);
        } catch (FileNotFoundException e) {
            model.addAttribute("text", "File not found!");
        } catch (IOException e) {
            model.addAttribute("text", "IOException occurred!");
        }

        // Set doc name
        model.addAttribute("curFile", curFile);
        // Set notes
        model.addAttribute("notes", noteService.getNotesByFilename(curFile));
    }

    private void updateFile(String changes) {
        File file = FileUtils.getFile(curDir, curFile);

        try {
            FileUtils.writeStringToFile(file, changes, Charset.defaultCharset());
        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND");
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
        }

    }

    private void setDirsAndFiles(Model model) {
        File[] dirs = curDir.listFiles(File::isDirectory);
        File[] files = curDir.listFiles(File::isFile);

        model.addAttribute("dirs", dirs);
        model.addAttribute("files", files);
    }

    @GetMapping("/")
    public String indexGet(Model model, @RequestParam(value="file_name", required=false) String fileName,
                                            @RequestParam(value="action", required = false) String action,
                                                @RequestParam(value="text", required = false) String text) {
        // Set current file to readme.txt if not specified
        curFile = (fileName == null) ? "readme.txt" : fileName;

        switch (action == null ? "open" : action) {
            case "new_file":
                text = "";
            case "save":
                updateFile(text);
            case "open":
                setDocAndNotes(model);
                break;
            case "delete":
                File file = FileUtils.getFile(curDir, curFile);
                FileUtils.deleteQuietly(file);
                curFile = "readme.txt";
                setDocAndNotes(model);
                break;
        }

        setDirsAndFiles(model);

        return "index";
    }

    @PostMapping("/")
    public String indexPost(Model model, @RequestParam(value="note_name") String noteName,
                                                @RequestParam(value="note_text") String noteText) {

        noteService.saveNote(new Note(noteName, noteText, curFile));

        setDocAndNotes(model);
        setDirsAndFiles(model);

        return "index";
    }

}
