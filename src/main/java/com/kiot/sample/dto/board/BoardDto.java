package com.kiot.sample.dto.board;

import com.kiot.sample.domain.File;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BoardDto {

    private Long id;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    private String writer;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String content;

    private Character deleteYn;

    private List<File> files;

    public BoardDto(Long id, String title, String writer, String content, Character deleteYn) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.deleteYn = deleteYn;
    }



}
