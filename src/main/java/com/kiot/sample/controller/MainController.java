package com.kiot.sample.controller;


import com.kiot.sample.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @RequestMapping("/")
    public String main() {
        return "main";
    }


    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    @RequestMapping("/board/write")
    public String boardWrite() {
        return "board/write";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
