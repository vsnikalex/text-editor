package com.epam.texteditor.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

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

    public Note(String header, String content) {
        update(header, content);
    }

    private void update(String header, String content) {
        this.header = header;
        this.content = content;
        this.modified = LocalDateTime.now();
    }

}
