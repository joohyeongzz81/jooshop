package com.example.jooshop.user.service;

import com.example.jooshop.user.domain.User;
import com.example.jooshop.user.domain.repository.UserRepository;
import com.example.jooshop.user.dto.request.UserJoinRequest;
import com.example.jooshop.user.dto.response.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원 가입 성공")
    void join_success() {
        // given
        UserJoinRequest request = new UserJoinRequest("test@example.com", "홍길동");
        User savedUser = new User(request.getEmail(), request.getName());
        ReflectionTestUtils.setField(savedUser, "id", 1L);

        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // when
        Long userId = userService.join(request);

        // then
        assertThat(userId).isNotNull();
        assertThat(userId).isEqualTo(1L);
        verify(userRepository).existsByEmail(request.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("이메일 중복 시 회원 가입 실패")
    void join_fail_duplicateEmail() {
        // given
        UserJoinRequest request = new UserJoinRequest("test@example.com", "홍길동");

        given(userRepository.existsByEmail(request.getEmail())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.join(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 이메일입니다.");

        verify(userRepository).existsByEmail(request.getEmail());
    }

    @Test
    @DisplayName("회원 조회 성공")
    void findById_success() {
        // given
        Long userId = 1L;
        User user = new User("test@example.com", "홍길동");
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        UserResponse response = userService.findById(userId);

        // then
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getName()).isEqualTo("홍길동");
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("존재하지 않는 회원 조회 시 예외 발생")
    void findById_fail_notFound() {
        // given
        Long userId = 1L;

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.findById(userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 회원입니다.");

        verify(userRepository).findById(userId);
    }
}