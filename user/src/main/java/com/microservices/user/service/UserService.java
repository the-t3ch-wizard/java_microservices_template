package com.microservices.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.microservices.user.dto.UserRequestDTO;
import com.microservices.user.dto.UserResponseDTO;
import com.microservices.user.dto.UserResponseWithPasswordDTO;
import com.microservices.user.exception.CustomException;
import com.microservices.user.model.User;
import com.microservices.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO saveOneUser(UserRequestDTO userDto){
        User newUser = User.builder()
            .username(userDto.getUsername())
            .email(userDto.getEmail())
            .password(userDto.getPassword())
            .build();

        userRepository.save(newUser);

        return UserResponseDTO.builder()
            .userId(newUser.getId())
            .email(newUser.getEmail())
            .username(newUser.getUsername())
            .createdAt(newUser.getCreatedAt())
            .build();
    }

    public UserResponseDTO getOneUser(UUID userId){
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException("No user found", HttpStatus.BAD_REQUEST));

        return UserResponseDTO.builder()
            .userId(userId)
            .email(user.getEmail())
            .username(user.getUsername())
            .createdAt(user.getCreatedAt())
            .build();
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
            .map(user -> UserResponseDTO.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
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
        existingUser.setUsername(newUserDto.getUsername());

        userRepository.save(existingUser);

        return UserResponseDTO.builder()
            .userId(userId)
            .email(existingUser.getEmail())
            .username(existingUser.getUsername())
            .createdAt(existingUser.getCreatedAt())
            .build();
    }

    public boolean deleteOneUser(UUID userId) {
        userRepository.deleteById(userId);
        return true;
    }

    public UserResponseWithPasswordDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new CustomException("No user found", HttpStatus.BAD_REQUEST);
        }
        
        return UserResponseWithPasswordDTO.builder()
            .userId(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .password(user.getPassword())
            .build();
    }
    
}
