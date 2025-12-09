package com.example.jooshop.user.presentation;

import com.example.jooshop.user.domain.User;
import com.example.jooshop.user.dto.request.UserJoinRequest;
import com.example.jooshop.user.dto.response.UserResponse;
import com.example.jooshop.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> join(@Valid @RequestBody UserJoinRequest request) {
        Long userId = userService.join(request.getEmail(), request.getName());

        return ResponseEntity.created(URI.create("/api/users/" + userId))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        UserResponse response = UserResponse.from(user);

        return ResponseEntity.ok(response);
    }
}