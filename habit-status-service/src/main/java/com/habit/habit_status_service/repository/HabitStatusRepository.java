package com.habit.habit_status_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.habit.habit_status_service.model.HabitStatus;

public interface HabitStatusRepository extends JpaRepository<HabitStatus, UUID> {
  
  // 

}
