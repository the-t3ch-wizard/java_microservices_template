package com.habit.habit_status_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private LocalDate date;

    @NotNull
    @Column(nullable = false)
    private Boolean done;

    @Size(max = 300)
    private String note;

    @Column(name = "habit_id", nullable = false)
    private UUID habitId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;
}
