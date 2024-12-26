package com.vijay.User_Master.service.impl;

import com.vijay.User_Master.dto.WorkerResponse;
import com.vijay.User_Master.repository.WorkerRepository;
import com.vijay.User_Master.service.WorkerUserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class WorkerUserServiceImpl implements WorkerUserService {

     private final WorkerRepository workerRepository;
    @Override
    public WorkerResponse findById(Long id) throws Exception {
        return null;
    }

    @Override
    public void softDelete(Long id) throws Exception {

    }

    @Override
    public void restore(Long id) throws Exception {

    }

    @Override
    public void hardDelete(Long id) throws Exception {

    }

    @Override
    public List<WorkerResponse> getRecycleBin() {
        return null;
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
    @Override
    public List<WorkerResponse> findAll() {
        return null;
    }
}
                   