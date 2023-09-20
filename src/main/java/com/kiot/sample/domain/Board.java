package com.kiot.sample.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Board extends Time{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;
    @Column(length = 10, nullable = false)
    private String writer;
    @Column(length = 1000, nullable = false)
    private String content;
    @Column(length = 1)
    @ColumnDefault("'N'")
    private Character deleteYn;

    @Builder
    public Board(Long id, String title, String writer, String content, Character deleteYn) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.deleteYn = deleteYn;
    }

    public void updateBoard(String title, String content){
        this.title = title;
        this.content = content;
    }
}
