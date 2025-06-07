package com.microservices.api_gateway.client;

import com.microservices.api_gateway.dto.SignUpRequest;
import com.microservices.api_gateway.dto.UserDtoResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserServiceClient {

  @PostMapping("/users")
  void createUser(@RequestBody SignUpRequest signup);

  @GetMapping("/users")
  UserDtoResponse getUserByUsername(@RequestParam String username);
    
}
