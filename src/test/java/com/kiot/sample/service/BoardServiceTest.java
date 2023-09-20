package com.kiot.sample.service;

import com.kiot.sample.domain.Board;
import com.kiot.sample.dto.board.BoardDto;
import com.kiot.sample.mapper.BoardMapper;
import com.kiot.sample.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardMapper boardMapper;
    @Test
    @Transactional
    @DisplayName("게시판 등록 구현 테스트")
    void createBoard() {

        //given
        BoardDto boardDto = new BoardDto();
        boardDto.setTitle("게시글 테스트입니당.");
        boardDto.setWriter("홍길동");
        boardDto.setContent("내용입니당.");

        //when
        Board board = boardMapper.toBoardEntity(boardDto);
        Board saveBoard = boardRepository.save(board);

        //then
        Assertions.assertThat(boardDto.getContent()).isNotEqualTo(saveBoard.getContent());
    }

    @Test
    @DisplayName("게시글 목록 조회 테스트")
    void getList() {

        //given
        Integer pageNum = 1;
        Integer PAGE_POST_COUNT = 5;

        //when
        PageRequest pageRequest = PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Board> boardPage = boardRepository.findAllByDeleteYn(pageRequest, 'N');
        List<Board> boardList = boardPage.getContent();

        //then
        Assertions.assertThat(boardList.get(0).getTitle()).isEqualTo("제목1");
        Assertions.assertThat(boardList.get(1).getTitle()).isEqualTo("제목2");
    }

    @Test
    @DisplayName("게시글 조회 테스트")
    void getBoard() {

        //given
        Long id = 202L;

        //when
        Board board = boardRepository.findByIdAndDeleteYn(id, 'N').orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));

        //then
        Assertions.assertThat(board.getId()).isEqualTo(id);
    }


    @Test
    @Transactional
    @DisplayName("게시글 삭제 테스트")
    void deleteBoard() {
        //given
        Long id= 5L;
        //when
        Board board = boardRepository.findByIdAndDeleteYn(id, 'N').orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));
        //then
        board.setDeleteYn('Y');
        Assertions.assertThat(board.getDeleteYn()).isEqualTo('N');

    }

    @Test
    @Transactional
    @DisplayName("게시글 수정 테스트")
    void updateBoard() {

        //given
        Long id = 2L;

        BoardDto boardDto = new BoardDto();
        boardDto.setTitle("게시글 수정 테스트입니당.");
        boardDto.setWriter("김수정");
        boardDto.setContent("내용도 수정 한 번 \n 해볼게요");

        //when
        Board findBoard = boardRepository.findByIdAndDeleteYn(id, 'N').orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));
        Board board = boardMapper.toBoardEntity(boardDto);
        Board afterSave = boardRepository.save(board);
        //then
        Assertions.assertThat(boardDto.getTitle()).isEqualTo(afterSave.getTitle());
    }

}