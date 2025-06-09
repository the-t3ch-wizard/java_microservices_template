package com.habit.habit_status_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class HabitStatusUpdateDTO {
  
  @NotNull
  private Boolean done;

  @Size(max = 300)
  private String note;
}
