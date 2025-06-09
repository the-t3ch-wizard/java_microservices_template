package com.habit.habit_status_service.client.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {
  private String message;

  // we’ll treat status as a String–eg "OK", "BAD_REQUEST", etc.
  private String status;

  private T data;
  private Boolean success;
}
