package com.vijay.User_Master.service.impl;

import com.vijay.User_Master.Helper.CommonUtils;
import com.vijay.User_Master.config.security.CustomUserDetails;
import com.vijay.User_Master.dto.WorkerResponse;
import com.vijay.User_Master.entity.AccountStatus;
import com.vijay.User_Master.entity.User;
import com.vijay.User_Master.entity.Worker;
import com.vijay.User_Master.exceptions.ResourceNotFoundException;
import com.vijay.User_Master.repository.UserRepository;
import com.vijay.User_Master.repository.WorkerRepository;
import com.vijay.User_Master.service.WorkerUserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class WorkerUserServiceImpl implements WorkerUserService {

    private final WorkerRepository workerRepository;
    private final ModelMapper mapper;
    private final UserRepository userRepository;

    // find user by id ... for Worker Entity
    @Override
    public WorkerResponse findById(Long id) throws Exception {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker", "ID", id));
        return mapper.map(worker, WorkerResponse.class);
    }

    // You can delete Item ... it saves at recycle bin.
    @Override
    public void softDelete(Long id) throws Exception {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker", "ID", id));
        if (worker.isDeleted()) {
            throw new IllegalArgumentException("Worker with ID " + id + " is already deleted.");
        }
        worker.setDeleted(true);
        worker.setDeletedOn(LocalDateTime.now());
        // Set accountStatus to inactive
        AccountStatus accountStatus = worker.getAccountStatus();
        accountStatus.setIsActive(false);
        worker.setAccountStatus(accountStatus);
        workerRepository.save(worker);
    }

    // You can restore Item form recycle bin
    @Override
    public void restore(Long id) throws Exception {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker", "ID", id));
        if (worker.isDeleted()) { // Worker is deleted, so restore it
            worker.setDeleted(false);
            worker.setDeletedOn(null);

            AccountStatus accountStatus = worker.getAccountStatus();
            accountStatus.setIsActive(true); // Set accountStatus to Active
            worker.setAccountStatus(accountStatus);

            workerRepository.save(worker); // Save the restored worker
        } else {
            throw new IllegalArgumentException("Worker with ID " + id + " is already present.");
        }
    }

    // You can delete Item form recycle bin - deleting permanently
    @Override
    public void hardDelete(Long id) throws Exception {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker", "ID", id));
        if (worker.isDeleted()) {
            workerRepository.delete(worker); // deleting form recycle bin
        } else {
            throw new IllegalArgumentException("Sorry You can't hard delete Directly");
        }
    }
    @Override
    public WorkerResponse copy(Long aLong) throws Exception {
        return null;
    }

    @Override
    public Page<WorkerResponse> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<WorkerResponse> search(String query, Pageable pageable) {
        return null;
    }

    @Override
    public void emptyRecycleBin() { //findByCreatedByAndIsDeletedTrue

    }

    // find all User from Worker user Entity
    @Override
    public List<WorkerResponse> findAll() {
        return workerRepository.findAll().stream()
                .map((worker -> mapper.map(worker, WorkerResponse.class)))
                .collect(Collectors.toList());
    }
    // find all only Active users by superuser id or loggedInUser userId
    @Override
    public List<WorkerResponse> findAllActiveUsers() {
        CustomUserDetails loggedInUser = CommonUtils.getLoggedInUser();
        List<Worker> userLists = workerRepository.findByCreatedByAndIsDeletedFalse(loggedInUser.getId());
        return userLists.stream()
                .map((worker -> mapper.map(worker, WorkerResponse.class)))
                .collect(Collectors.toList());
    }
    // find all only Deleted users by superuser id or loggedInUser userId
    @Override
    public List<WorkerResponse> getRecycleBin() { // restore delete item from RecycleBin
        CustomUserDetails loggedInUser = CommonUtils.getLoggedInUser();
        List<Worker> listUser = workerRepository.findByCreatedByAndIsDeletedTrue(loggedInUser.getId());
        if (listUser.isEmpty()) {
            throw new ResourceNotFoundException("Recycle Bin", "Workers", "No deleted workers found for the current user.");
        }
        return listUser.stream()
                .map(worker -> mapper.map(worker, WorkerResponse.class))
                .collect(Collectors.toList());
    }
}
                   