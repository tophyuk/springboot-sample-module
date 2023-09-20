package com.kiot.sample.handler;

import com.kiot.sample.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto> illegalExHandle(IllegalArgumentException e) {
        log.error("[IllegalArgumentException] ex", e);
        ResponseDto<Object> responseDto = ResponseDto.builder()
                .code("F002")
                .message(e.getMessage())
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDto> typeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("[MethodArgumentTypeMismatchException] ex", e);

        ResponseDto<Object> responseDto = ResponseDto.builder()
                .code("F003")
                .message("잘못된 요청입니다.")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException] ex", e);

        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> list = new ArrayList<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            map.put(fieldName, message);
        });

        list.add(map);

        ResponseDto<Object> responseDto = ResponseDto.builder()
                .code("F003")
                .message("잘못된 요청입니다.")
                .data(new ArrayList<>(list))
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> usernameNotFoundException(UsernameNotFoundException e) {
        log.error("[MethodArgumentNotValidException] ex", e);

        ResponseDto<Object> responseDto = ResponseDto.builder()
                .code("F003")
                .message("존재하지 않는 아이디입니다.")
                .data(null)
                .build();


        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }
}
