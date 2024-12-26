package com.vijay.User_Master.repository;

import com.vijay.User_Master.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {


    Worker findByEmail(String email);

    Optional<Worker> findByUsernameOrEmail(String username, String email);

    Optional<Worker> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
