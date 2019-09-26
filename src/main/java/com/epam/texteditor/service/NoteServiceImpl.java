package com.epam.texteditor.service;

import com.epam.texteditor.model.Note;
import com.epam.texteditor.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    public Map<String, List<Note>> getNotesByFileGroupByHeaderSortByDate(File file) {
        List<Note> noteList = noteRepository.findAllByFile(file);

        // Notes with the same name are considered to be the same Note
        // => need to group by name to represent them at JSP
        Map<String, List<Note>> noteMap = noteList.stream()
                                                  .collect(Collectors.groupingBy(Note::getHeader));

        // Sort by date reversed to show recent first at JSP
        noteMap.forEach((k, v) -> { v.sort(Comparator.comparing(Note::getModified).reversed()); });

        return noteMap;
    }

    @Override
    public void deleteNotesByFile(File file) {
        noteRepository.deleteNotesByFile(file);
    }

    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }
}
