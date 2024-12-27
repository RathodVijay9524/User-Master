package com.vijay.User_Master.repository;

import com.vijay.User_Master.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long>, JpaSpecificationExecutor<Worker> {


    Worker findByEmail(String email);

    Optional<Worker> findByUsernameOrEmail(String username, String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);


    List<Worker> findByCreatedByAndIsDeletedFalse(Long id);

    // Find all workers created by a specific user and marked as deleted
    List<Worker> findByCreatedByAndIsDeletedTrue(Long userId);

    // Find all workers by a specific status
    List<Worker> findByAccountStatus_IsActive(Boolean isActive);

    // Find all workers by name containing a specific keyword (case insensitive)
    List<Worker> findByNameContainingIgnoreCase(String name);

    // Find all workers by username
    List<Worker> findByUsername(String username);
}


