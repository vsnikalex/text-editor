package com.epam.texteditor.service;

import com.epam.texteditor.model.Note;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

public interface NoteService {

    void saveNote(Note note);

    List<Note> getNotesByFile(File file);

    void deleteNotesByFile(File file);

    List<Note> getAllNotes();

}
