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
import java.util.HashMap;
import java.util.Map;


@Controller
public class MainController {

    private final NoteService noteService;
    private final ApplicationContext context;

    private File root;
    // Scope: directory-file browser, directory notes
    private File curDir;
    // Scope: file editor, file notes
    private File curFile;

    public MainController(ConfigurableApplicationContext context, NoteService noteService) {
        this.context = context;

        // At start, application focuses on root directory
        root = (File) context.getBean("root");
        EditorUtils.makeReadOnly(root);
        curDir = root;
        curFile = curDir;

        this.noteService = noteService;
    }

    @SneakyThrows(IOException.class)
    private void setDocAndNotes(Model model) {
        // Scope: file editor, file notes

        // curFile is a directory after opening the app (curFile is root directory by default)
        // and after deleting a file (curFile will be set to curDir)
        if (curFile.isDirectory()) {
            model.addAttribute("text", "");
            model.addAttribute("curFileIsDir", true);
        } else {
            String encoding = EditorUtils.getFileEncoding(curFile);
            if (encoding == null) {
                encoding = "UTF-8";
            }

            String text = FileUtils.readFileToString(curFile,  Charset.forName(encoding));

            model.addAttribute("text", text);
            model.addAttribute("textNotes", noteService.getNotesByFileGroupByHeaderSortByDate(curFile));
        }

        // Set document name and access
        model.addAttribute("filePath", curFile.getAbsolutePath());
        model.addAttribute("filePathHashCode", curFile.getAbsolutePath().hashCode());
        model.addAttribute("canWrite", curFile.canWrite());
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

            // if file is without extension or there is not appropriate icon, use default one
            int i = fName.lastIndexOf(".");
            String extension = (i == -1) ? "default" : fName.substring(i);
            String img = icons.containsKey(extension) ? icons.get(extension) : icons.get("default");

            filesAndIcons.put(fName, img);
        }

        model.addAttribute("isRoot", curDir.equals(root));
        model.addAttribute("curDir", curDir);
        model.addAttribute("readOnly", EditorUtils.isReadOnly(curDir));

        model.addAttribute("dirs", dirs);
        model.addAttribute("dirNotes", noteService.getNotesByFileGroupByHeaderSortByDate(curDir));
        model.addAttribute("filesAndIcons", filesAndIcons);
    }

    private boolean removeDirOrFile(File file) {
        if (EditorUtils.isReadOnly(file)) { return false; }

        noteService.deleteNotesByFile(file);
        return FileUtils.deleteQuietly(file);
    }

    @GetMapping("/")
    @SneakyThrows
    public String indexGet(Model model,     @RequestParam(value="action", required = false) String action,

                                            @RequestParam(value="file_name", required=false) String fileName,
                                            @RequestParam(value="file_access", required = false) String fileAccess,
                                            @RequestParam(value="text", required = false) String text,

                                            @RequestParam(value="dir_name", required = false) String dirName,
                                            @RequestParam(value="dir_access", required = false) String dirAccess) {

        if (action != null)
        switch (action) {
            case "new_file":
                curFile = new File(curDir, fileName);
                EditorUtils.updateOrCreateFile(curFile, text);
                if ("read_only".equals(fileAccess)) {
                    EditorUtils.makeReadOnly(curFile);
                }
                break;
            case "open_file":
                curFile = new File(curDir, fileName);
                break;
            case "rm_file":
                if (removeDirOrFile(curFile)) {
                    curFile = curDir;
                }
                break;
            case "new_dir":
                File newDir = new File(curDir.getAbsolutePath(), dirName);
                if (!newDir.exists()) {
                    newDir.mkdir();
                    if ("read_only".equals(dirAccess)) {
                        EditorUtils.makeReadOnly(newDir);
                    }
                }
                break;
            case "open_dir":
                curDir = new File(curDir, dirName);
                break;
            case "go_back":
                curDir = new File(curDir.getParent());
                break;
            case "rm_dir":
                if (removeDirOrFile(curDir)) {
                    curDir = curDir.getParentFile();
                }
                break;
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
