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

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserResponseDTO saveOneUser(UserRequestDTO userDto){
        User newUser = User.builder()
            .name(userDto.getName())
            .email(userDto.getEmail())
            .password(userDto.getPassword())
            .build();

        userRepository.save(newUser);

        return UserResponseDTO.builder()
            .userId(newUser.getId())
            .email(newUser.getEmail())
            .name(newUser.getName())
            .createdAt(newUser.getCreatedAt())
            .build();
    }

    public UserResponseDTO getOneUser(UUID userId){
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException("No user found", HttpStatus.BAD_REQUEST));

        return UserResponseDTO.builder()
            .userId(userId)
            .email(user.getEmail())
            .name(user.getName())
            .createdAt(user.getCreatedAt())
            .build();
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
            .map(user -> UserResponseDTO.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .createdAt(user.getCreatedAt())
                .build())
            .toList();
    }

    public UserResponseDTO updateOneUser(UUID userId, UserRequestDTO newUserDto) {
        User existingUser = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException("No user found", HttpStatus.BAD_REQUEST));

        // password will be changed in another api
        // email will be changed in another api
        // so, currently only name can be changed
        existingUser.setName(newUserDto.getName());

        userRepository.save(existingUser);

        return UserResponseDTO.builder()
            .userId(userId)
            .email(existingUser.getEmail())
            .name(existingUser.getName())
            .createdAt(existingUser.getCreatedAt())
            .build();
    }

    public boolean deleteOneUser(UUID userId) {
        userRepository.deleteById(userId);
        return true;
    }
    
}
