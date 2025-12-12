package com.example.jooshop.user.service;

import com.example.jooshop.user.domain.User;
import com.example.jooshop.user.domain.repository.UserRepository;
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
        String email = "test@example.com";
        String name = "홍길동";
        User savedUser = new User(email, name);
        ReflectionTestUtils.setField(savedUser, "id", 1L);

        given(userRepository.existsByEmail(email)).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // when
        Long userId = userService.join(email, name);

        // then
        assertThat(userId).isNotNull();
        assertThat(userId).isEqualTo(1L);
        verify(userRepository).existsByEmail(email);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("이메일 중복 시 회원 가입 실패")
    void join_fail_duplicateEmail() {
        // given
        String email = "test@example.com";
        String name = "홍길동";

        given(userRepository.existsByEmail(email)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.join(email, name))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 이메일입니다.");

        verify(userRepository).existsByEmail(email);
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
        User foundUser = userService.findById(userId);

        // then
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
        assertThat(foundUser.getName()).isEqualTo("홍길동");
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