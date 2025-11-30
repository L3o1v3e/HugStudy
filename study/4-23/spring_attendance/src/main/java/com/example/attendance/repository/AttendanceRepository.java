package com.example.attendance.repository;

import com.example.attendance.entity.AttendanceEntity;
import com.example.attendance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {

    /** 一覧（勤務日降順） */
    List<AttendanceEntity> findAllByOrderByWorkDateDesc();

    /** 社員 × 勤務日で検索（出退勤判定用） */
    Optional<AttendanceEntity> findByEmployeeAndWorkDate(UserEntity employee, LocalDate workDate);
}
