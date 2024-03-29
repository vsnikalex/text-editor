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

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


@Controller
public class MainController {

    private final NoteService noteService;
    private final ApplicationContext context;
    private final EditorUtils editorUtils;

    private File root;
    private Map<String, String> supported;

    public MainController(ConfigurableApplicationContext context, NoteService noteService, EditorUtils editorUtils) {
        this.context = context;
        this.noteService = noteService;
        this.editorUtils = editorUtils;

        root = (File) context.getBean("root");
        this.editorUtils.makeReadOnly(root);

        supported = (HashMap<String, String>) context.getBean("supportedFilesAndIcons");
    }

    private void setDocAndNotes(Model model, HttpSession session) {
        // Scope: file editor, file notes
        File curFile = (File)session.getAttribute("curFile");

        // curFile is a directory after opening the app (curFile is root directory by default)
        // and after deleting a file (curFile will be set to curDir)
        // it won't exist after deleting from OS manually
        if (!curFile.exists() || curFile.isDirectory()) {
            if (!curFile.exists()) { session.setAttribute("curFile", curFile.getParentFile()); }
            model.addAttribute("text", "");
            model.addAttribute("curFileIsDir", true);
        } else {
            String extension = editorUtils.extension(curFile.getName());
            boolean supported = editorUtils.supported(extension);
            String text = supported ? editorUtils.checkEncodingAndRead(curFile) :
                    "FILE EXTENSION IS NOT SUPPORTED BY EDITOR";

            model.addAttribute("text", text);
            model.addAttribute("supported", supported);
            model.addAttribute("textNotes", noteService.getNotesByFileGroupByHeaderSortByDate(curFile));
        }

        // Set document name and access
        model.addAttribute("filePath", curFile.getAbsolutePath());
        model.addAttribute("filePathHashCode", curFile.getAbsolutePath().hashCode());
        model.addAttribute("canWrite", curFile.canWrite());
    }

    private void setDirsAndFiles(Model model, HttpSession session) {
        // Scope: file browser, directory notes
        File curDir = (File)session.getAttribute("curDir");

        File[] dirs = curDir.listFiles(File::isDirectory);

        // Chose file ico via XML configuration depending on extension
        File[] files = curDir.listFiles(File::isFile);
        Map<String, String> icons = supported;

        Map<String, String> filesAndIcons = new HashMap<>();
        for (File f : files) {
            String fName = f.getName();

            // if file is without extension or there is not appropriate icon, use default one
            String extension = editorUtils.extension(fName);
            String img = icons.containsKey(extension) ? icons.get(extension) : icons.get("default");

            filesAndIcons.put(fName, img);
        }

        model.addAttribute("isRoot", curDir.equals(root));
        model.addAttribute("curDirName", curDir.getName());
        model.addAttribute("curDirPath", curDir.getAbsolutePath());
        model.addAttribute("readOnly", editorUtils.isReadOnly(curDir));

        model.addAttribute("dirs", dirs);
        model.addAttribute("dirNotes", noteService.getNotesByFileGroupByHeaderSortByDate(curDir));
        model.addAttribute("filesAndIcons", filesAndIcons);
    }

    private boolean removeDirOrFile(File file) {
        if (editorUtils.isReadOnly(file)) { return false; }

        noteService.deleteNotesByFile(file);
        return FileUtils.deleteQuietly(file);
    }

    @GetMapping("/")
    @SneakyThrows
    public String indexGet(Model model, HttpSession session,
                                            @RequestParam(value="action", required = false) String action,

                                            @RequestParam(value="file_name", required=false) String fileName,
                                            @RequestParam(value="file_access", required = false) String fileAccess,
                                            @RequestParam(value="text", required = false) String text,

                                            @RequestParam(value="dir_name", required = false) String dirName,
                                            @RequestParam(value="dir_access", required = false) String dirAccess) {

        // Default values
        if (session.isNew()) {
            // Scope: directory-file browser, directory notes
            session.setAttribute("curDir", root);
            // Scope: file editor, file notes
            session.setAttribute("curFile", root);
        }

        // Actual values
        File curDir = (File)session.getAttribute("curDir");
        File curFile = (File)session.getAttribute("curFile");

        if (action != null)
            switch (action) {
                case "new_file":
                    session.setAttribute("curFile", new File(curDir, fileName));
                    curFile = (File) session.getAttribute("curFile");
                    editorUtils.updateOrCreateFile(curFile, text);
                    if ("read_only".equals(fileAccess)) {
                        editorUtils.makeReadOnly(curFile);
                    }
                    break;
                case "open_file":
                    File fileToOpen = new File(curDir, fileName);
                    if (fileToOpen.exists()) {
                        session.setAttribute("curFile", fileToOpen);
                    }
                    break;
                case "rm_file":
                    if (removeDirOrFile(curFile)) {
                        session.setAttribute("curFile", curDir);
                    }
                    break;
                case "new_dir":
                    File newDir = new File(curDir.getAbsolutePath(), dirName);
                    if (!newDir.exists()) {
                        newDir.mkdir();
                        if ("read_only".equals(dirAccess)) {
                            editorUtils.makeReadOnly(newDir);
                        }
                    }
                    break;
                case "open_dir":
                    File dirToOpen = new File(curDir, dirName);
                    if (dirToOpen.exists()) {
                        session.setAttribute("curDir", dirToOpen);
                    }
                    break;
                case "go_back":
                    if (!curFile.equals(root)) {
                        session.setAttribute("curDir", curDir.getParentFile());
                    }
                    break;
                case "rm_dir":
                    if (removeDirOrFile(curDir)) {
                        session.setAttribute("curDir", curDir.getParentFile());
                    }
                    break;
            }

        setDocAndNotes(model, session);
        setDirsAndFiles(model, session);

        return "index";
    }

    @PostMapping("/")
    public String indexPost(Model model, HttpSession session,
                                            @RequestParam(value="note_name") String noteName,
                                            @RequestParam(value="note_text") String noteText,
                                            @RequestParam(value="note_type") String noteType) {

        switch (noteType) {
            case "text_note":
                noteService.saveNote(new Note(noteName, noteText, (File) session.getAttribute("curFile")));
                break;
            case "dir_note":
                noteService.saveNote(new Note(noteName, noteText, (File) session.getAttribute("curDir")));
                break;
        }

        setDocAndNotes(model, session);
        setDirsAndFiles(model, session);

        return "index";
    }

}
