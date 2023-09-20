package com.kiot.sample.mapper;

import com.kiot.sample.domain.Board;
import com.kiot.sample.dto.board.BoardDto;
import org.springframework.stereotype.Component;

@Component
public class BoardMapper {

    public BoardDto toBoardDto(Board board) {
        return new BoardDto(
                board.getId(),
                board.getTitle(),
                board.getWriter(),
                board.getContent(),
                board.getDeleteYn()
        );
    }

    public Board toBoardEntity(BoardDto boardDto) {
        return Board.builder()
                .id(boardDto.getId())
                .title(boardDto.getTitle())
                .writer(boardDto.getWriter())
                .content(boardDto.getContent())
                .deleteYn(boardDto.getDeleteYn())
                .build();
    }



}
