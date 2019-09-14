package com.epam.texteditor.controller;

import com.epam.texteditor.model.Note;
import com.epam.texteditor.service.NoteService;
import lombok.SneakyThrows;
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

    private final NoteService noteService;

    // curDir: file browser, directory notes
    // curFile: file editor, file notes
    private File root;
    private File curDir;
    private File curFile;

    public MainController(ConfigurableApplicationContext context, NoteService noteService) {
        root = (File) context.getBean("root");
        curDir = root;
        curFile = curDir;
        this.noteService = noteService;
    }

    @SneakyThrows(IOException.class)
    private void setDocAndNotes(Model model) {
        // Scope: file editor, file notes

        // True after opening the app (curFile is root directory by default)
        // True after deleting file (curFile will be set to curDir)
        if (curFile.isDirectory()) {
            model.addAttribute("text", "");
        } else {
            String text = FileUtils.readFileToString(curFile, Charset.defaultCharset());
            model.addAttribute("text", text);
        }

        // Set document name
        model.addAttribute("curFile", curFile.getAbsolutePath());
        // Set text notes
        model.addAttribute("textNotes", noteService.getNotesByFile(curFile));
    }

    @SneakyThrows(IOException.class)
    private void updateFile(String changes) {

        FileUtils.writeStringToFile(curFile, changes, Charset.defaultCharset());

    }

    private void setDirsAndFiles(Model model) {
        // Scope: file browser, directory notes
        File[] dirs = curDir.listFiles(File::isDirectory);
        File[] files = curDir.listFiles(File::isFile);

        model.addAttribute("dirs", dirs);
        model.addAttribute("files", files);

        // Set directory notes
        model.addAttribute("dirNotes", noteService.getNotesByFile(curDir));
    }

    @GetMapping("/")
    public String indexGet(Model model, @RequestParam(value="file_name", required=false) String fileName,
                                            @RequestParam(value="action", required = false) String action,
                                            @RequestParam(value="text", required = false) String text,
                                            @RequestParam(value="dir_name", required = false) String dirName,
                                            @RequestParam(value="back", required = false) String back) {

        // Create | Save | Open | Delete file
        if (fileName != null) { curFile = new File(curDir, fileName); }
        switch (action == null ? "open" : action) {
            case "new_file":
                text = "";
            case "save":
                updateFile(text);
            case "open":
                setDocAndNotes(model);
                break;
            case "delete":
                FileUtils.deleteQuietly(curFile);
                curFile = curDir;
                setDocAndNotes(model);
                break;
        }

        // Open | Create directory
        if (dirName != null) {
            File dir = new File(curDir.getAbsolutePath(), dirName);
            if (dir.exists()) {
                curDir = dir;
            } else {
                dir.mkdir();
            }
        } else if (back != null) {
            // Don't move higher than root
            File moveBack = new File(curDir.getParent());
            if (!moveBack.equals(root.getParentFile())) {
                curDir = moveBack;
            }
        }
        setDirsAndFiles(model);

        return "index";
    }

    @PostMapping("/")
    public String indexPost(Model model, @RequestParam(value="note_name") String noteName,
                                            @RequestParam(value="note_text") String noteText,
                                            @RequestParam(value="note_type") String noteType) {

        switch (noteType) {
            case "text_note":
                noteService.saveNote(new Note(noteName, noteText, curFile));
                break;
            case "dir_note":
                noteService.saveNote(new Note(noteName, noteText, curDir));
                break;
        }

        setDocAndNotes(model);
        setDirsAndFiles(model);

        return "index";
    }

}
