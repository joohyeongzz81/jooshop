package com.example.jooshop.user.service;

import com.example.jooshop.global.exception.BadRequestException;
import com.example.jooshop.user.domain.User;
import com.example.jooshop.user.domain.repository.UserRepository;
import com.example.jooshop.user.dto.request.UserJoinRequest;
import com.example.jooshop.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.jooshop.global.exception.ExceptionCode.DUPLICATE_EMAIL;
import static com.example.jooshop.global.exception.ExceptionCode.NOT_FOUND_USER_ID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long join(final UserJoinRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(DUPLICATE_EMAIL);
        }

        User user = new User(request.getEmail(), request.getName());
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public UserResponse findById(final Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_USER_ID));
        return UserResponse.from(user);
    }
}