package com.habit.habit_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.habit.habit_service.dto.HabitRequestDTO;
import com.habit.habit_service.dto.HabitResponseDTO;
import com.habit.habit_service.exception.CustomException;
import com.habit.habit_service.model.Habit;
import com.habit.habit_service.repository.HabitRepository;

import jakarta.transaction.Transactional;

@Service
public class HabitService {

  @Autowired
  private HabitRepository habitRepository;

  private HabitResponseDTO mapHabitToHabitResponse(Habit h) {
    return HabitResponseDTO.builder()
      .id(h.getId())
      .name(h.getName())
      .description(h.getDescription())
      .categoryName(h.getCategoryName())
      .userId(h.getUserId())
      .createdAt(h.getCreatedAt())
      .build();
  }

  @Transactional
  public HabitResponseDTO createHabit(HabitRequestDTO habitDto) {
    // TODO 
    // validate that only current user and valid user can create habit

    Habit h = Habit.builder()
      .name(habitDto.getName())
      .description(habitDto.getDescription())
      .categoryName(habitDto.getCategoryName())
      .userId(habitDto.getUserId())
      .build();

    habitRepository.save(h);

    return mapHabitToHabitResponse(h);
  }

  public HabitResponseDTO getOneHabit(UUID id) {

    System.out.println("id CAME --------------" + id);
    Habit h = habitRepository.findById(id)
      .orElseThrow(() -> new CustomException("No Habit found", HttpStatus.BAD_REQUEST));
    
    System.out.println("id CAME -------------- 2 " + h);
    return mapHabitToHabitResponse(h);
  }

  public List<HabitResponseDTO> getAllHabits() {
    List<Habit> habits = habitRepository.findAll();

    return habits.stream()
      .map((habit) -> {
        return mapHabitToHabitResponse(habit);
      })
      .toList();
  }

  @Transactional
  public HabitResponseDTO updateOneHabit(UUID id, HabitRequestDTO dto) {
    Habit h = habitRepository.findById(id)
      .orElseThrow(() -> new CustomException("No Habit found", HttpStatus.BAD_REQUEST));

    h.setName(dto.getName());
    h.setDescription(dto.getDescription());
    h.setCategoryName(dto.getCategoryName());

    habitRepository.save(h);

    return mapHabitToHabitResponse(h);
  }

  @Transactional
  public void deleteOneHabit(UUID id) {
    Habit h = habitRepository.findById(id)
      .orElseThrow(() -> new CustomException("No Habit found", HttpStatus.BAD_REQUEST));
    
    habitRepository.delete(h);
  }

  public List<HabitResponseDTO> getAllHabitsOfUser(UUID userId) {
    List<Habit> habits = habitRepository.findByUserId(userId);

    return habits.stream()
      .map((habit) -> {
        return mapHabitToHabitResponse(habit);
      })
      .toList();
  }

}
