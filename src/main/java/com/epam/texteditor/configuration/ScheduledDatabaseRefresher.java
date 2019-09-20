package com.epam.texteditor.configuration;

import com.epam.texteditor.model.Note;
import com.epam.texteditor.service.NoteService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledDatabaseRefresher {

    private final NoteService noteService;

    public ScheduledDatabaseRefresher(NoteService noteService) {
        this.noteService = noteService;
    }

    @Scheduled( initialDelay = 3 * 1000, fixedDelay = 2 * 1000)
    public void refresh() {
        // Get list of files from DB, compare to actual files, remove notes for absents
        noteService.getAllNotes().stream()
                                    .map(Note::getFile)
                                    .distinct()
                                    .forEach(f -> {
                                        if (!f.exists()) {
                                            noteService.deleteNotesByFile(f);
                                        }
                                    });
    }

}
