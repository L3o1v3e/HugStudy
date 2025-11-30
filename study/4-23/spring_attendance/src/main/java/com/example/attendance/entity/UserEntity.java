package com.example.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "\"user\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    /** 社員ID（主キー） */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long employeeId;

    /** 社員名（ユニーク制約） */
    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
