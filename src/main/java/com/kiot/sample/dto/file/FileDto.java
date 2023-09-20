package com.kiot.sample.dto.file;

import com.kiot.sample.domain.File;
import jakarta.persistence.JoinColumn;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class FileDto {

    private Long id;
    private Long boardId;
    private String origFileName;
    private String fileName;
    private String filePath;

    public FileDto(Long id, Long boardId, String origFileName, String fileName, String filePath) {
        this.id = id;
        this.boardId= boardId;
        this.origFileName = origFileName;
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
