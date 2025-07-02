package com.edtech.entity;

import com.edtech.enums.CourseCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String thumbnail;
    private String description;
    private Double price;
    private String curriculum;
    private String duration;

    @Enumerated(EnumType.STRING)
    private CourseCategory category;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;
}
