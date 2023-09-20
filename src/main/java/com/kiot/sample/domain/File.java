package com.kiot.sample.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false)
    private String origFileName;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @Builder
    public File(Long id, Board board, String origFileName, String fileName, String filePath) {
        this.id = id;
        this.board = board;
        this.origFileName = origFileName;
        this.fileName = fileName;
        this.filePath = filePath;
    }
}