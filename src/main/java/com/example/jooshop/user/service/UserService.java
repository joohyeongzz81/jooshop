package com.example.jooshop.user.service;

import com.example.jooshop.user.domain.User;
import com.example.jooshop.user.domain.repository.UserRepository;
import com.example.jooshop.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long join(String email, String name) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        User user = new User(email, name);
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return UserResponse.from(user);
    }
}