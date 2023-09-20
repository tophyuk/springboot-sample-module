package com.kiot.sample.service;

import com.kiot.sample.dto.file.FileDto;
import com.kiot.sample.mapper.FileMapper;
import com.kiot.sample.repository.FileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

    /** 파일 저장 **/
    @Transactional
    public Long saveFile(MultipartFile files, Long boardId) throws IOException, NoSuchAlgorithmException {

        // 파일 이름,경로 설정
        String origFilename = files.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String extension = origFilename.substring(origFilename.lastIndexOf("."));
        String filename = uuid + extension;

        // 실행되는 위치의 'files' 폴더에 파일이 저장
        String savePath = System.getProperty("user.dir") + "/files";

        // 파일이 저장되는 폴더가 없으면 폴더를 생성
        if (!new File(savePath).exists()) {
            try{
                new File(savePath).mkdir();
            }
            catch(Exception e){
                e.getStackTrace();
            }
        }

        // 서버에 저장
        String filepath = savePath + "/" + filename;
        files.transferTo(new File(filepath));

        // DB에 file 저장
        FileDto fileDto = new FileDto();
        fileDto.setOrigFileName(origFilename);
        fileDto.setBoardId(boardId);
        fileDto.setFileName(filename);
        fileDto.setFilePath(filepath);

        com.kiot.sample.domain.File file = fileMapper.toFileEntity(fileDto);
        Long id = fileRepository.save(file).getId();
        return id;
    }

    /** 파일들 조회 **/
    public List<com.kiot.sample.domain.File> getFiles(Long boardId) {
        List<com.kiot.sample.domain.File> files = fileRepository.findByBoardId(boardId);
        return files;
    }

    public void deleteFile(Long fileId) {

        // todo - 폴더에서 파일 삭제
        com.kiot.sample.domain.File file = fileRepository.findById(fileId).orElseThrow(() -> new IllegalArgumentException("해당 파일은 존재하지 않습니다."));

        try {
            String srcFileName = URLDecoder.decode(file.getFileName(), "UTF-8");
            File f = new File(System.getProperty("user.dir") + "/files" + File.separator + srcFileName);
            f.delete();
        }  catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } {

        }

        // todo - DB에서 파일 삭제
        fileRepository.delete(file);

    }

    @Transactional
    public InputStreamResource downloadFile() throws FileNotFoundException {
        // 실행되는 위치의 'files' 폴더에 파일이 저장
        String savePath = System.getProperty("user.dir") + "/files";
        Path filePath = Paths.get(savePath);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(filePath.toString()));
        return resource;
    }
}
