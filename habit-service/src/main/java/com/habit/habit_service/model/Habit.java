package com.habit.habit_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @Size(max = 500)
    private String description;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    private String categoryName;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;
}
