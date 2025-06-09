package com.habit.habit_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habit.habit_service.dto.HabitRequestDTO;
import com.habit.habit_service.dto.HabitResponseDTO;
import com.habit.habit_service.service.HabitService;
import com.habit.habit_service.utils.SuccessResponseHandler;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/habits")
public class HabitController {
  
  @Autowired
  private HabitService habitService;

  @PostMapping
  public ResponseEntity<?> createHabit(@Valid @RequestBody HabitRequestDTO habitDto) {
    HabitResponseDTO newHabit = habitService.createHabit(habitDto);
    return SuccessResponseHandler.generaResponseEntity("Habit created successfully", HttpStatus.CREATED, newHabit);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getHabit(@PathVariable UUID id) {
    HabitResponseDTO user = habitService.getOneHabit(id);
    return SuccessResponseHandler.generaResponseEntity("Habit fetched successfully", HttpStatus.OK, user);
  }

  @GetMapping("/all/{userId}")
  public ResponseEntity<?> listHabitsOfUser(@PathVariable UUID userId) {
    List<HabitResponseDTO> all = habitService.getAllHabitsOfUser(userId);
    return SuccessResponseHandler.generaResponseEntity("All habits of user's fetched successfully", HttpStatus.OK, all);
  }

  @GetMapping
  public ResponseEntity<?> listHabits() {
    List<HabitResponseDTO> all = habitService.getAllHabits();
    return SuccessResponseHandler.generaResponseEntity("All habits fetched successfully", HttpStatus.OK, all);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateHabit(@PathVariable UUID id, @Valid @RequestBody HabitRequestDTO dto) {
    HabitResponseDTO updated = habitService.updateOneHabit(id, dto);
    return SuccessResponseHandler.generaResponseEntity("Habit updated successfully", HttpStatus.OK, updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteHabit(@PathVariable UUID id) {
    habitService.deleteOneHabit(id);
    return SuccessResponseHandler.generaResponseEntity("Habit deleted successfully", HttpStatus.OK, null);
  }

}
