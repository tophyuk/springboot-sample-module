package com.kiot.sample.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ResponseDto<T> {
    private String code;
    private String message;
    private Object data;
    private PageResponseDto pageResponseDto;

    @Builder
    public ResponseDto(String code, String message, Object data, PageResponseDto pageResponseDto) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.pageResponseDto = pageResponseDto;
    }
}
