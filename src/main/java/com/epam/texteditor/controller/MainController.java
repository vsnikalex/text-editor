package com.epam.texteditor.controller;

import com.epam.texteditor.model.Note;
import com.epam.texteditor.service.NoteService;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
public class MainController {

    private final NoteService noteService;
    private final ApplicationContext context;

    // curDir: file browser, directory notes
    // curFile: file editor, file notes
    private File root;
    private File curDir;
    private File curFile;

    public MainController(ConfigurableApplicationContext context, NoteService noteService) {
        this.context = context;

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
        // If file is writable or new
        if (curFile.canWrite() || !curFile.exists()) {
            FileUtils.writeStringToFile(curFile, changes, Charset.defaultCharset());
        }
    }

    private void setDirsAndFiles(Model model) {
        // Scope: file browser, directory notes
        File[] dirs = curDir.listFiles(File::isDirectory);

        // Chose file ico via XML configuration depending on extension
        File[] files = curDir.listFiles(File::isFile);
        Map<String, String> icons = (HashMap<String, String>) context.getBean("icons");
        Map<String, String> filesAndIcons = Arrays.stream(files)
                                .collect(Collectors.toMap(File::getName,
                                        f -> {  String fName = f.getName();
                                                String extension = fName.substring(fName.lastIndexOf("."));
                                                if (icons.containsKey(extension)) {
                                                    return icons.get(extension);
                                                } else {
                                                    return icons.get("default");
                                                }}));

        model.addAttribute("dirs", dirs);
        model.addAttribute("filesAndIcons", filesAndIcons);

        // Set directory notes
        model.addAttribute("dirNotes", noteService.getNotesByFile(curDir));
    }

    @GetMapping("/")
    @SneakyThrows
    public String indexGet(Model model, @RequestParam(value="file_name", required=false) String fileName,
                                            @RequestParam(value="file_access", required = false) String fileAccess,
                                            @RequestParam(value="action", required = false) String action,
                                            @RequestParam(value="text", required = false) String text,
                                            @RequestParam(value="dir_name", required = false) String dirName,
                                            @RequestParam(value="dir_access", required = false) String dirAccess,
                                            @RequestParam(value="back", required = false) String back) {

        // Change file focus
        if (fileName != null && !fileName.isEmpty()) { curFile = new File(curDir, fileName); }
        // Create | Save | Open | Delete file, Delete directory
        // When opening the first time action will be null: default "open" is used
        switch (action == null ? "open" : action) {
            case "new_file":
                updateFile(text);
                if ("read_only".equals(fileAccess)) {
                    curFile.setReadOnly();
                }
                break;
            case "save":
                updateFile(text);
            case "open":
                // App start action can be added here
                break;
            case "delete":
                noteService.deleteNotesByFile(curFile);
                FileUtils.deleteQuietly(curFile);
                curFile = curDir;
                break;
            case "rm_dir":
                if (!curDir.equals(root)) {
                    noteService.deleteNotesByFile(curDir);
                    FileUtils.deleteQuietly(curDir);
                    curDir = curDir.getParentFile();
                }
                break;
        }

        // Open | Create directory
        if (dirName != null) {
            File dir = new File(curDir.getAbsolutePath(), dirName);
            if (dir.exists()) {
                curDir = dir;
            } else {
                dir.mkdir();
                // TODO: Prohibit directory creation if dirAccess=read_only
//                if ("read_only".equals(dirAccess)) {
//                    dir.setWritable(false);
//                }
            }
        } else if (back != null) {
            // Don't move higher than root
            File moveBack = new File(curDir.getParent());
            if (!moveBack.equals(root.getParentFile())) {
                curDir = moveBack;
            }
        }

        setDocAndNotes(model);
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
