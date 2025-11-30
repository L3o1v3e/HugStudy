package com.example.attendance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private UserEntity employee;

    @Column(nullable = false)
    private String employeeName;

    @Column(nullable = false)
    private LocalDate workDate;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
}
