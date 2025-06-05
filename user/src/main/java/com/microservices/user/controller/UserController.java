package com.microservices.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.user.dto.UserRequestDTO;
import com.microservices.user.service.UserService;
import com.microservices.user.utils.SuccessResponseHandler;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("")
    public ResponseEntity<?> postOneUser(@Valid @RequestBody UserRequestDTO userDto){
        return SuccessResponseHandler.generaResponseEntity("User created successfully", HttpStatus.CREATED, userService.saveOneUser(userDto));
    }

    // this is being used in auth service so, no change in response format
    // Todo : match response in auth based on normal response
    @GetMapping("")
    public ResponseEntity<?> getUserByUsername(@RequestParam String username){
        // return SuccessResponseHandler.generaResponseEntity("User fetched successfully", HttpStatus.ACCEPTED, userService.getUserByUsername(username));
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(){
        return SuccessResponseHandler.generaResponseEntity("All users fetched successfully", HttpStatus.ACCEPTED, userService.getAllUsers());
    }

    @PutMapping("")
    public ResponseEntity<?> putOneUser(@RequestParam String username, @Valid @RequestBody UserRequestDTO userDto){
        return SuccessResponseHandler.generaResponseEntity("User updated successfully", HttpStatus.ACCEPTED, userService.updateOneUser(username, userDto));
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteOneUser(@RequestParam String username){
        return SuccessResponseHandler.generaResponseEntity("User deleted successfully", HttpStatus.ACCEPTED, userService.deleteOneUser(username));
    }

}
