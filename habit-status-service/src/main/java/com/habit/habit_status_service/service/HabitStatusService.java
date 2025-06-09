package com.habit.habit_status_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.habit.habit_status_service.client.HabitServiceFeignClient;
import com.habit.habit_status_service.client.dto.ApiResponse;
import com.habit.habit_status_service.client.dto.HabitResponseDTO;
import com.habit.habit_status_service.dto.HabitStatusRequestDTO;
import com.habit.habit_status_service.dto.HabitStatusResponseDTO;
import com.habit.habit_status_service.dto.HabitStatusUpdateDTO;
import com.habit.habit_status_service.exception.CustomException;
import com.habit.habit_status_service.model.HabitStatus;
import com.habit.habit_status_service.repository.HabitStatusRepository;

import jakarta.transaction.Transactional;

@Service
public class HabitStatusService {

  @Autowired
  private HabitStatusRepository habitStatusRepository;

  @Autowired
  private HabitServiceFeignClient habitService;

  @Transactional
  public HabitStatusResponseDTO createHabitStatus(HabitStatusRequestDTO dto) {
    // TODO 
    // validate that only current user and valid user can create habit

    System.out.println("TEST -> " + dto.getHabitId());
    System.out.println("TEST FINAL -> " + dto);


    // ApiResponse<HabitResponseDTO> testHabit = habitService.getOneHabit(UUID.fromString("9b5d5331-9e86-4e82-9a0f-9c2056340fe6"));

    // System.out.println("--------= ===== " + testHabit);

    ApiResponse<HabitResponseDTO> habit = habitService.getOneHabit(dto.getHabitId());

    if (habit.getData() == null){
      throw new CustomException("No habit found with id "+dto.getHabitId(), HttpStatus.BAD_REQUEST);
    }

    System.out.println("habit is -> " + habit);

    System.out.println("->" + dto.getDate());
    
    HabitStatus h = HabitStatus.builder()
      .date(dto.getDate())
      .done(dto.getDone())
      .note(dto.getNote())
      .habitId(habit.getData().getId())
      .build();
    
    System.out.println("->" + h.getDate());
    System.out.println("->" + h.getDone());
    System.out.println("->" + h.getNote());
    System.out.println("->" + h.getCreatedAt());
    System.out.println("->" + h.getHabitId());

    habitStatusRepository.save(h);

    return mapToPublic(h);
  }

  public HabitStatusResponseDTO getOneHabitStatus(UUID id) {

    HabitStatus h = habitStatusRepository.findById(id)
      .orElseThrow(() -> new CustomException("No record found", HttpStatus.BAD_REQUEST));

    return mapToPublic(h);
  }

  public List<HabitStatusResponseDTO> getAllHabitStatuses() {

    List<HabitStatus> habitStatuses = habitStatusRepository.findAll();

    return habitStatuses.stream()
      .map((habit) -> {
        return mapToPublic(habit);
      })
      .toList();
  }

  @Transactional
  public HabitStatusResponseDTO toggleOneHabitStatus(UUID id) {

    HabitStatus h = habitStatusRepository.findById(id)
      .orElseThrow(() -> new CustomException("No record found", HttpStatus.BAD_REQUEST));

    h.setDone(!h.getDone());

    habitStatusRepository.save(h);

    return mapToPublic(h);
  }

  @Transactional
  public HabitStatusResponseDTO updateOneHabitStatus(UUID id, HabitStatusUpdateDTO dto) {

    HabitStatus h = habitStatusRepository.findById(id)
      .orElseThrow(() -> new CustomException("No record found", HttpStatus.BAD_REQUEST));

    h.setDone(dto.getDone());
    h.setNote(dto.getNote());

    habitStatusRepository.save(h);

    return mapToPublic(h);
  }

  private HabitStatusResponseDTO mapToPublic(HabitStatus h) {
    return HabitStatusResponseDTO.builder()
      .id(h.getId())
      .date(h.getDate())
      .done(h.getDone())
      .note(h.getNote())
      .habitId(h.getHabitId())
      .createdAt(h.getCreatedAt())
      .build();
  }

}
