package com.microservices.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.microservices.user.dto.UserRequestDTO;
import com.microservices.user.dto.UserResponseDTO;
import com.microservices.user.exception.CustomException;
import com.microservices.user.model.User;
import com.microservices.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDTO saveOneUser(UserRequestDTO userDto){
        User newUser = User.builder()
            .username(userDto.getUsername())
            .email(userDto.getEmail())
            .password(userDto.getPassword())
            .role(userDto.getRole())
            .build();

        userRepository.save(newUser);

        return UserResponseDTO.builder()
            .userId(newUser.getId())
            .username(newUser.getUsername())
            .email(newUser.getEmail())
            .password(newUser.getPassword())
            .role(newUser.getRole())
            .createdAt(newUser.getCreatedAt())
            .build();
    }

    public UserResponseDTO getOneUser(UUID userId){
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException("No user found", HttpStatus.BAD_REQUEST));

        return UserResponseDTO.builder()
            .userId(userId)
            .username(user.getUsername())
            .email(user.getEmail())
            .password(user.getPassword())
            .role(user.getRole())
            .createdAt(user.getCreatedAt())
            .build();
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
            .map(user -> UserResponseDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build())
            .toList();
    }

    @Transactional
    public UserResponseDTO updateOneUser(String username, UserRequestDTO newUserDto) {
        User existingUser = userRepository.findByUsername(username);

        if (existingUser == null) throw new CustomException("No user found", HttpStatus.BAD_REQUEST);

        // password will be changed in another api
        // email will be changed in another api
        // so, currently only name can be changed
        existingUser.setUsername(newUserDto.getUsername());

        userRepository.save(existingUser);

        return UserResponseDTO.builder()
            .userId(existingUser.getId())
            .username(existingUser.getUsername())
            .email(existingUser.getEmail())
            .password(existingUser.getPassword())
            .role(existingUser.getRole())
            .createdAt(existingUser.getCreatedAt())
            .build();
    }

    @Transactional
    public boolean deleteOneUser(String username) {
        userRepository.deleteByUsername(username);
        return true;
    }

    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new CustomException("No user found", HttpStatus.BAD_REQUEST);
        }
        
        return UserResponseDTO.builder()
            .userId(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .password(user.getPassword())
            .role(user.getRole())
            .createdAt(user.getCreatedAt())
            .build();
    }
    
}
