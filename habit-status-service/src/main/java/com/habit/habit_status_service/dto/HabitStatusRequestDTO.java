package com.habit.habit_status_service.dto;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class HabitStatusRequestDTO {

  @NotNull
  private LocalDate date;

  @NotNull
  private Boolean done;

  @NotNull
  @Size(max = 300)
  private String note;

  @NotNull
  private UUID habitId;
}
