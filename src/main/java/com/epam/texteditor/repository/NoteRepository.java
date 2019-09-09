package com.epam.texteditor.repository;

import com.epam.texteditor.model.Note;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

    List<Note> findAllByOrderByModifiedDesc();

    List<Note> findAllByFilepathOrderByModifiedDesc(String filepath);

}
