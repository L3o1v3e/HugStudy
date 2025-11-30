package com.example.attendance.repository;

import com.example.attendance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /** 社員名で検索（例：山田　太郎） */
	Optional<UserEntity> findByName(String name);

}
