package com.microservices.user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.microservices.user.dto.AuthUserDTO;
import com.microservices.user.dto.UserRequestDTO;
import com.microservices.user.dto.UserResponseDTO;
import com.microservices.user.service.UserService;
import com.microservices.user.utils.SuccessResponseHandler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO created = userService.saveOneUser(dto);
        return SuccessResponseHandler.generaResponseEntity("User created successfully", HttpStatus.CREATED, created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable UUID id) {
        UserResponseDTO user = userService.getOneUser(id);
        return SuccessResponseHandler.generaResponseEntity("User fetched successfully", HttpStatus.OK, user);
    }

    @GetMapping
    public ResponseEntity<?> listUsers() {
        List<UserResponseDTO> all = userService.getAllUsers();
        return SuccessResponseHandler.generaResponseEntity("All users fetched successfully", HttpStatus.OK, all);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO updated = userService.updateOneUser(id, dto);
        return SuccessResponseHandler.generaResponseEntity("User updated successfully", HttpStatus.OK, updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        userService.deleteOneUser(id);
        return SuccessResponseHandler.generaResponseEntity("User deleted successfully", HttpStatus.OK, null);
    }

    // used by auth-service (returns password)
    @GetMapping("/search")
    public ResponseEntity<AuthUserDTO> getByUsername(@RequestParam String username) {
        AuthUserDTO auth = userService.getAuthUserByUsername(username);
        return ResponseEntity.ok(auth);
    }
}
