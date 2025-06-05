package com.microservices.auth.client;

import com.microservices.auth.dto.SignUpRequest;
import com.microservices.auth.dto.UserDtoResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @PostMapping("/users")
    void createUser(@RequestBody SignUpRequest signup);

    @GetMapping("/users")
    UserDtoResponse getUserByUsername(@RequestParam String username);
    
}
