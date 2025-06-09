package com.habit.habit_status_service.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.habit.habit_status_service.client.dto.ApiResponse;
import com.habit.habit_status_service.client.dto.HabitResponseDTO;

@FeignClient(name = "habit-service")
public interface HabitServiceFeignClient {
  
  @GetMapping("/habits/{habitId}")
  ApiResponse<HabitResponseDTO> getOneHabit(@PathVariable UUID habitId);

}
