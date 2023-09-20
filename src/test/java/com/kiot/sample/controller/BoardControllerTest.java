package com.kiot.sample.controller;

import com.kiot.sample.dto.board.BoardDto;
import com.kiot.sample.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = BoardController.class)
class BoardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean   // @Autowired아님 --> BoardService 테스트를 위해 가짜 객체를 쓰겠다는 뜻
    BoardService boardService; // 가짜 객체를 쓰면 좋은점 DB와 상관없이 테스트 가능

    private String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaGo4Mjc4IiwiaWF0IjoxNjgxMzY5OTU2LCJleHAiOjE2ODEzNzAwMTZ9.FB3jtphBL6mHdUsrs1rN6SE8ZvwO1YAN5VUS8Jf2BBc";
    @Test
    @DisplayName("게시판 등록 호출 테스트")
    public void createBoardApiTest() throws Exception {
        //given

        BoardDto boardDto = new BoardDto();
        boardDto.setTitle("게시글 테스트입니당.");
        boardDto.setWriter("홍길동");
        boardDto.setContent("내용입니당");


        //when
        boardService.createBoard(boardDto);

        //상태 코드와 메시지 바디 검증
        //String url = String.format("/api/v1/hospitals/%d", hospitalId);
        mockMvc.perform(post("/board")
                .header("Authorization", "Bearer " + token))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.code").value("S002"))
                .andDo(print());
    }

    @Test
    @DisplayName("게시판 목록 호출 테스트")
    public void getBoard() throws Exception {
        mockMvc.perform(get("/board/list/page=1")
                .contentType("application/json;charset=utf-8")
                .accept("application/json;charset=utf-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}