package com.habit.habit_status_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habit.habit_status_service.dto.HabitStatusRequestDTO;
import com.habit.habit_status_service.dto.HabitStatusResponseDTO;
import com.habit.habit_status_service.dto.HabitStatusUpdateDTO;
import com.habit.habit_status_service.service.HabitStatusService;
import com.habit.habit_status_service.utils.SuccessResponseHandler;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/habit-status")
public class HabitStatusController {
  
  @Autowired
  private HabitStatusService habitService;

  @PostMapping
  public ResponseEntity<?> createHabitStatus(@Valid @RequestBody HabitStatusRequestDTO habitDto) {
    HabitStatusResponseDTO newHabit = habitService.createHabitStatus(habitDto);
    return SuccessResponseHandler.generaResponseEntity("Habit status created successfully", HttpStatus.CREATED, newHabit);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getHabitStatus(@PathVariable UUID id) {
    HabitStatusResponseDTO user = habitService.getOneHabitStatus(id);
    return SuccessResponseHandler.generaResponseEntity("Habit statuse fetched successfully", HttpStatus.OK, user);
  }

  @GetMapping
  public ResponseEntity<?> listHabitStatuses() {
    List<HabitStatusResponseDTO> all = habitService.getAllHabitStatuses();
    return SuccessResponseHandler.generaResponseEntity("All habit statuses fetched successfully", HttpStatus.OK, all);
  }

  @PutMapping("/toggle/{id}")
  public ResponseEntity<?> toggleHabitStatus(@PathVariable UUID id) {
    HabitStatusResponseDTO updated = habitService.toggleOneHabitStatus(id);
    return SuccessResponseHandler.generaResponseEntity("Habit status toggled successfully", HttpStatus.OK, updated);
  }

  @PutMapping("/u/{id}")
  public ResponseEntity<?> updateHabitStatus(@PathVariable UUID id, @Valid @RequestBody HabitStatusUpdateDTO dto) {
    HabitStatusResponseDTO updated = habitService.updateOneHabitStatus(id, dto);
    return SuccessResponseHandler.generaResponseEntity("Habit status updated successfully", HttpStatus.OK, updated);
  }

}
