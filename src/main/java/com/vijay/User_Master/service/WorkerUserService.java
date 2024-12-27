package com.vijay.User_Master.service;

import com.vijay.User_Master.dto.UserResponse;

import com.vijay.User_Master.dto.WorkerResponse;
import com.vijay.User_Master.service.generics.AdvancedCrudService;

import java.util.List;


public interface WorkerUserService extends AdvancedCrudService<WorkerResponse, Long> {

    public void emptyRecycleBin();  //findByCreatedByAndIsDeletedTrue
    List<WorkerResponse> findAllActiveUsers();

}
