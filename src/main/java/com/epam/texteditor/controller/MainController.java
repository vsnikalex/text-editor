package com.epam.texteditor.controller;

import com.epam.texteditor.model.Note;
import com.epam.texteditor.service.NoteService;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.mozilla.universalchardet.UniversalDetector;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.util.HashMap;
import java.util.Map;


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
        makeReadOnly(root);
        curDir = root;
        curFile = curDir;

        this.noteService = noteService;
    }

    @SneakyThrows(IOException.class)
    private static String getFileEncoding(File file) {
        UniversalDetector detector = new UniversalDetector(null);
        byte[] bytes = FileUtils.readFileToByteArray(file);
        detector.handleData(bytes, 0, bytes.length);
        detector.dataEnd();
        return detector.getDetectedCharset();
    }

    @SneakyThrows(IOException.class)
    private static void makeReadOnly(File file) {
        // this method is required because file.setReadOnly() doesn't work for directory
        Path path = file.toPath();
        Files.setAttribute(path, "dos:readonly", true);
    }

    @SneakyThrows(IOException.class)
    private static boolean isReadOnly(File file) {
        // this method is required because file.canWrite() always returns true for directory
        Path path = file.toPath();
        DosFileAttributes dfa = Files.readAttributes(path, DosFileAttributes.class);
        return dfa.isReadOnly();
    }

    @SneakyThrows(IOException.class)
    private void setDocAndNotes(Model model) {
        // Scope: file editor, file notes

        // curFile is a directory after opening the app (curFile is root directory by default)
        // and after deleting a file (curFile will be set to curDir)
        if (!curFile.exists() || curFile.isDirectory()) {
            model.addAttribute("text", "");
        } else {
            String encoding = getFileEncoding(curFile);
            if (encoding == null) {
                encoding = "UTF-8";
            }

            String text = FileUtils.readFileToString(curFile,  Charset.forName(encoding));
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

        Map<String, String> filesAndIcons = new HashMap<>();
        for (File f : files) {
            String fName = f.getName();

            String extension = fName.substring(fName.lastIndexOf("."));
            String img = icons.containsKey(extension) ? icons.get(extension) : icons.get("default");

            filesAndIcons.put(fName, img);
        }

        model.addAttribute("dirs", dirs);
        model.addAttribute("dirNotes", noteService.getNotesByFile(curDir));
        model.addAttribute("filesAndIcons", filesAndIcons);

        model.addAttribute("isRoot", curDir.equals(root));
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
                    makeReadOnly(curFile);
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
                // root is also readOnly (set in constructor)
                if (!isReadOnly(curDir)) {
                    noteService.deleteNotesByFile(curDir);
                    FileUtils.deleteQuietly(curDir);
                    curDir = curDir.getParentFile();
                }
                break;
        }

        // Open | Create directory
        if (dirName != null) {
            File newDir = new File(curDir.getAbsolutePath(), dirName);
            if (newDir.exists()) {
                // Open
                curDir = newDir;
            } else {
                newDir.mkdir();
                if ("read_only".equals(dirAccess)) {
                    makeReadOnly(newDir);
                }
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
