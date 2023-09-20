package com.kiot.sample.service;

import com.kiot.sample.domain.Board;
import com.kiot.sample.domain.File;
import com.kiot.sample.dto.board.BoardDto;
import com.kiot.sample.dto.SearchDto;
import com.kiot.sample.mapper.BoardMapper;
import com.kiot.sample.repository.BoardRepository;
import com.kiot.sample.repository.FileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;
    private final BoardMapper boardMapper;
    private final int PAGE_POST_COUNT = 5; // 한 페이지에 존재하는 게시글 수

    public Page<Board> getBoardList(Integer pageNum, SearchDto searchDto) {

        PageRequest pageRequest = PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<Board> boardList = null;
        if (searchDto.getSearchType() == null || searchDto.getKeyword() == null) {
            boardList = boardRepository.findAllByDeleteYn(pageRequest, 'N');
        } else if (searchDto.getSearchType().equals("title")) {
            boardList = boardRepository.findByTitleContainingAndDeleteYn(searchDto.getKeyword(), 'N', pageRequest);
        } else if (searchDto.getSearchType().equals("content")) {
            boardList = boardRepository.findByContentContainingAndDeleteYn(searchDto.getKeyword(), 'N', pageRequest);
        } else if (searchDto.getSearchType().equals("writer")) {
            boardList = boardRepository.findByWriterContainingAndDeleteYn(searchDto.getKeyword(), 'N', pageRequest);
        }

        return boardList;
    }

    public BoardDto getBoard(Long id) {
        Board board = boardRepository.findByIdAndDeleteYn(id, 'N').orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));
        BoardDto boardDto = boardMapper.toBoardDto(board);

        return boardDto;
    }


    @Transactional
    public Long createBoard(BoardDto boardDto) {
        boardDto.setDeleteYn('N');
        Board board = boardMapper.toBoardEntity(boardDto);
        Board saveBoard = boardRepository.save(board);
        return saveBoard.getId();
    }

    @Transactional
    public void deleteBoard(Long id) {
        Board board = boardRepository.findByIdAndDeleteYn(id, 'N').orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));
        board.setDeleteYn('Y');
    }
    @Transactional
    public void updateBoard(Long id, @NotNull BoardDto boardDto) {
        Board board = boardRepository.findByIdAndDeleteYn(id, 'N').orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));
        board.updateBoard(boardDto.getTitle(), boardDto.getContent());
    }

}
