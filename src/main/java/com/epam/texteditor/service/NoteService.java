package com.epam.texteditor.service;

import com.epam.texteditor.model.Note;

import java.util.List;

public interface NoteService {

    List<Note> getAllNotes();

    void saveNote(Note node);

}
