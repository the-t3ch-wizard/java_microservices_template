package com.habit.habit_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.habit.habit_service.model.Habit;

public interface HabitRepository extends JpaRepository<Habit, UUID> {

  List<Habit> findByUserId(UUID userId);
  
  // 

}
