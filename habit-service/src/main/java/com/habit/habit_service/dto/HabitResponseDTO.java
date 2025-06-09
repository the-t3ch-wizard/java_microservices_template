package com.habit.habit_service.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class HabitResponseDTO {
  
  private UUID id;
  private String name;
  private String description;
  private String categoryName;
  private UUID userId;
  private LocalDate createdAt;

}
