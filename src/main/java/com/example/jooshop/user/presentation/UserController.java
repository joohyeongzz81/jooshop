package com.example.jooshop.user.presentation;

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
    public ResponseEntity<Void> join(@Valid @RequestBody final UserJoinRequest request) {
        Long userId = userService.join(request);
        return ResponseEntity.created(URI.create("/api/users/" + userId))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable final Long id) {
        UserResponse response = userService.findById(id);
        return ResponseEntity.ok(response);
    }
}