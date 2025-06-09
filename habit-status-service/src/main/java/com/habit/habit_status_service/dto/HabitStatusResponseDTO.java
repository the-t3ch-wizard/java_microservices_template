package com.habit.habit_status_service.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class HabitStatusResponseDTO {
  
  private UUID id;
  private LocalDate date;
  private Boolean done;
  private String note;
  private UUID habitId;
  private LocalDate createdAt;

}
