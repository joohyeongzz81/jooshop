package com.example.jooshop.user.presentation;

import com.example.jooshop.global.config.WebMvcConfig;
import com.example.jooshop.user.domain.User;
import com.example.jooshop.user.dto.request.UserJoinRequest;
import com.example.jooshop.user.dto.response.UserResponse;
import com.example.jooshop.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(WebMvcConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("회원 가입 성공")
    void join_success() throws Exception {
        // given
        UserJoinRequest request = new UserJoinRequest("test@example.com", "홍길동");
        given(userService.join(anyString(), anyString())).willReturn(1L);

        // when & then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/users/1"));
    }

    @Test
    @DisplayName("회원 가입 실패 - 이메일 형식 오류")
    void join_fail_invalidEmail() throws Exception {
        // given
        UserJoinRequest request = new UserJoinRequest("invalid-email", "홍길동");

        // when & then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 가입 실패 - 이름 길이 초과")
    void join_fail_nameTooLong() throws Exception {
        // given
        String longName = "a".repeat(101);
        UserJoinRequest request = new UserJoinRequest("test@example.com", longName);

        // when & then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 조회 성공")
    void getUser_success() throws Exception {
        // given
        User user = new User("test@example.com", "홍길동");
        UserResponse response = UserResponse.from(user);
        given(userService.findById(1L)).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("홍길동"));
    }
}