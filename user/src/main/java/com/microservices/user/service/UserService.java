package com.microservices.user.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservices.user.dto.AuthUserDTO;
import com.microservices.user.dto.UserRequestDTO;
import com.microservices.user.dto.UserResponseDTO;
import com.microservices.user.exception.CustomException;
import com.microservices.user.model.User;
import com.microservices.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;

    @Transactional
    public UserResponseDTO saveOneUser(UserRequestDTO dto) {
        if (repo.existsByUsername(dto.getUsername())) {
            throw new CustomException("Username already taken", HttpStatus.BAD_REQUEST);
        }

        User u = User.builder()
            .username(dto.getUsername())
            .email(dto.getEmail())
            .password(dto.getPassword())
            .role(dto.getRole())
            .build();

        repo.save(u);
        return mapToPublic(u);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getOneUser(UUID userId) {
        User u = repo.findById(userId)
            .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        return mapToPublic(u);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return repo.findAll().stream()
                   .map(this::mapToPublic)
                   .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO updateOneUser(UUID userId, UserRequestDTO dto) {
        User u = repo.findById(userId)
            .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        u.setUsername(dto.getUsername());
        u.setEmail(dto.getEmail());
        // for security, do not overwrite password here
        repo.save(u);
        return mapToPublic(u);
    }

    @Transactional
    public void deleteOneUser(UUID userId) {
        if (!repo.existsById(userId)) {
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }
        repo.deleteById(userId);
    }

    @Transactional(readOnly = true)
    public AuthUserDTO getAuthUserByUsername(String username) {
        User u = repo.findByUsername(username);
        if (u == null) {
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }
        return AuthUserDTO.builder()
            .userId(u.getId())
            .username(u.getUsername())
            .email(u.getEmail())
            .password(u.getPassword())
            .role(u.getRole())
            .createdAt(u.getCreatedAt())
            .build();
    }

    private UserResponseDTO mapToPublic(User u) {
        return UserResponseDTO.builder()
            .userId(u.getId())
            .username(u.getUsername())
            .email(u.getEmail())
            .role(u.getRole())
            .createdAt(u.getCreatedAt())
            .updatedAt(u.getUpdatedAt())
            .build();
    }
}
