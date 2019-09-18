package com.epam.texteditor.repository;

import com.epam.texteditor.model.Note;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

    List<Note> findAllByFileOrderByModifiedDesc(File file);

    List<Note> findAll();

    @Transactional
    void deleteNotesByFile(File file);

}
