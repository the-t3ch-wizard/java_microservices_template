package com.microservices.user.utils;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private boolean success;
    private String message;
    private int status;
    private ErrorDetails data;

    @Getter @Setter @AllArgsConstructor @NoArgsConstructor
    public static class ErrorDetails {
        private LocalDateTime timestamp;
        private String path;
    }

}
