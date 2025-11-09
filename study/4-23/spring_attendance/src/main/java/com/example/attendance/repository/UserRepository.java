package com.example.attendance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	/**
	 * 社員名で検索
	 */
	Optional<User> findByName(String name);
}
