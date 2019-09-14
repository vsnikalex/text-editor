package com.epam.texteditor.service;

import com.epam.texteditor.model.Note;

import java.io.File;
import java.util.List;

public interface NoteService {

    void saveNote(Note note);

    List<Note> getNotesByFile(File file);

}
