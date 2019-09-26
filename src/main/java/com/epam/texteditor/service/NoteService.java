package com.epam.texteditor.service;

import com.epam.texteditor.model.Note;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface NoteService {

    void saveNote(Note note);

    Map<String, List<Note>> getNotesByFileGroupByHeaderSortByDate(File file);

    void deleteNotesByFile(File file);

    List<Note> getAllNotes();

}
