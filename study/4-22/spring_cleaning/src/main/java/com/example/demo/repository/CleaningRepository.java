package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.CleaningEntity;

/**
* 掃除情報 Repository
*/
@Repository
public interface CleaningRepository extends JpaRepository<CleaningEntity, Integer> {
}