package com.kiot.sample.controller;

import com.kiot.sample.dto.board.BoardDto;
import com.kiot.sample.dto.file.FileDto;
import com.kiot.sample.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @GetMapping("/download/{file}")
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
    public void deleteFile(@PathVariable Long id) {
        log.info("@!@!");
        //todo - 파일 삭제
        fileService.deleteFile(id);

    }
}
