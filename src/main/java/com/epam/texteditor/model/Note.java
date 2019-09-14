package com.epam.texteditor.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDateTime;


@Entity
@Table(name = "notes")
@NoArgsConstructor
@Data
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "header")
    private String header;

    @Column(name = "content")
    private String content;

    @Column(name = "modified")
    private LocalDateTime modified;

    @Column(name = "file")
    private File file;

    public Note(String header, String content, File file) {
        this.header = header;
        this.content = content;
        this.modified = LocalDateTime.now();
        this.file = file;
    }

}
