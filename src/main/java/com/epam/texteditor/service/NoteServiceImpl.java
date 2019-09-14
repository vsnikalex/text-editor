package com.epam.texteditor.service;

import com.epam.texteditor.model.Note;
import com.epam.texteditor.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public void saveNote(Note note) {
        noteRepository.save(note);
    }

    @Override
    public List<Note> getNotesByFile(File file) {
        return noteRepository.findAllByFileOrderByModifiedDesc(file);
    }
}
