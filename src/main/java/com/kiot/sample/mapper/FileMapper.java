package com.kiot.sample.mapper;

import com.kiot.sample.domain.Board;
import com.kiot.sample.domain.File;
import com.kiot.sample.dto.file.FileDto;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {

    public FileDto toFileDto(File file) {
        return new FileDto(
                file.getId(),
                file.getBoard().getId(),
                file.getOrigFileName(),
                file.getFileName(),
                file.getFilePath()
                );
    }

    public File toFileEntity(FileDto fileDto) {
        File file = File.builder()
                .id(fileDto.getId())
                .origFileName(fileDto.getOrigFileName())
                .fileName(fileDto.getFileName())
                .filePath(fileDto.getFilePath())
                .build();

/*        File file = new File();
        file.setId(fileDto.getId());
        file.setFileName(fileDto.getFileName());
        file.setFilePath(fileDto.getFilePath());*/

        Board board = Board.builder()
                .id(fileDto.getId())
                .build();

        board.setId(fileDto.getBoardId());
        file.setBoard(board);

        return file;
    }

}
