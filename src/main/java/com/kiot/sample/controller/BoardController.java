package com.kiot.sample.controller;

import com.kiot.sample.domain.Board;
import com.kiot.sample.domain.File;
import com.kiot.sample.dto.board.BoardDto;
import com.kiot.sample.dto.ResponseDto;
import com.kiot.sample.dto.SearchDto;
import com.kiot.sample.dto.file.FileDto;
import com.kiot.sample.service.BoardService;
import com.kiot.sample.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;


    @GetMapping("/list/{page}")
    public Page<Board> list(@PathVariable("page") @Min(0) Integer page,
                            SearchDto searchDto) throws Exception{

        Page<Board> boardPage = boardService.getBoardList(page, searchDto);

        /** 아래 주석은 Page 정보 Response에 담아서 보내기  **/
/*
        List<Board> boardList = boardPage.getContent(); // Page<> -> List<>로 수정

        PageResponseDto pageResponseDto = PageResponseDto.builder()
                .pageNum(boardPage.getNumber())
                .totalPages(boardPage.getTotalPages())
                .totalCnt(boardPage.getTotalElements())
                .build();

        ResponseDto responseDto = ResponseDto.builder()
                .code("S002")
                .message("게시판 전체 조회에 성공하였습니다.")
                .data(new ArrayList<>(boardList))
                .pageResponseDto(pageResponseDto)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.ok);
*/
        return boardPage;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getBoard(@PathVariable("id") Long id) {

        BoardDto boardDto = boardService.getBoard(id);
        List<File> files = fileService.getFiles(id);
        boardDto.setFiles(files);

        ResponseDto<Object> responseDto = ResponseDto.builder()
                .code("S002")
                .message("게시글 조회에 성공하였습니다")
                .data(boardDto)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ResponseDto> createBoard(@Validated BoardDto boardDto, @RequestParam(value="files", required = false) List<MultipartFile> files) throws IOException, NoSuchAlgorithmException {

        // 게시판 등록
        Long boardId = boardService.createBoard(boardDto);

        // 첨부파일 등록
        if (files != null) {
            for (MultipartFile multipartFile : files) {
                fileService.saveFile(multipartFile, boardId);
            }
        }

        ResponseDto<Object> responseDto = ResponseDto.builder()
                .code("S002")
                .message("게시글 등록에 성공하였습니다")
                .data(null)
                .build();


        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);

    }

    @GetMapping("/fileDownload/{file}")
    public void fileDownLoad(@PathVariable String file,
                             HttpServletResponse response) throws IOException {

        String savePath = System.getProperty("user.dir") + "/files";;
        java.io.File newFile = new java.io.File(savePath, file);

        response.setContentType("application/download");
        response.setContentLength((int)newFile.length());
        response.setHeader("Content-disposition", "attachment;filename=\"" + file + "\"");
        // response 객체를 통해서 서버로부터 파일 다운로드
        OutputStream os = response.getOutputStream();
        // 파일 입력 객체 생성
        FileInputStream fis = new FileInputStream(newFile);
        FileCopyUtils.copy(fis, os);
        fis.close();
        os.close();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteBoard(@PathVariable Long id) throws Exception {

        boardService.deleteBoard(id);

        //todo - 파일들 삭제

        ResponseDto responseDto = ResponseDto.builder()
                .code("S002")
                .message("게시판 삭제에 성공하였습니다.")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateBoard(@PathVariable Long id, @RequestBody BoardDto boardDto) {

        boardService.updateBoard(id, boardDto);

        ResponseDto responseDto = ResponseDto.builder()
                .code("S002")
                .message("게시판 수정에 성공하였습니다.")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }
}
