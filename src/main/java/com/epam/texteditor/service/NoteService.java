package com.epam.texteditor.service;

import com.epam.texteditor.model.Note;

import java.util.List;

public interface NoteService {

    List<Note> getAllNotes();

    List<Note> getNotesByFilename(String filepath);

    void saveNote(Note node);

}
