package com.example.attendance.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.Attendance;
import com.example.attendance.entity.User;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	// 社員(User)と勤務日で検索
	Optional<Attendance> findByEmployeeAndWorkDate(User employee, LocalDate workDate);
}
