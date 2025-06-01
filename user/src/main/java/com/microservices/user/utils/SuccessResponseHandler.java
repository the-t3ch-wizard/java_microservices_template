package com.microservices.user.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SuccessResponseHandler {

    public static ResponseEntity<Object> generaResponseEntity(String message, HttpStatus status, Object data){
        Map<String, Object> response = new HashMap<>();
        
        response.put("message", message);
        response.put("status", status);
        response.put("data", data);
        response.put("success", true);

        return new ResponseEntity<>(response, status);
    }
    
}
