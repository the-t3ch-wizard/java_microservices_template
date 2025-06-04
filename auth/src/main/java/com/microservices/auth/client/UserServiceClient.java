package com.microservices.auth.client;

import com.microservices.auth.dto.SignUpRequest;
import com.microservices.auth.dto.UserDtoResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Feign will use Eureka to resolve "user-service" → host:port automatically.
 * All methods are blocking (i.e. return types are plain objects, not Mono/Flux).
 */
@FeignClient(name = "user-service")
public interface UserServiceClient {

    @PostMapping("/users")
    void createUser(@RequestBody SignUpRequest signup);

    @GetMapping("/users")
    UserDtoResponse getUserByUsername(@RequestParam String username);
    
}
