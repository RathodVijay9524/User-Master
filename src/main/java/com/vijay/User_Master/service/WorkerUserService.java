package com.vijay.User_Master.service;

import com.vijay.User_Master.dto.PageableResponse;
import com.vijay.User_Master.dto.UserResponse;

import com.vijay.User_Master.dto.WorkerResponse;
import com.vijay.User_Master.service.generics.AdvancedCrudService;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface WorkerUserService extends AdvancedCrudService<WorkerResponse, Long> {

    
    List<WorkerResponse> findAllActiveUsers();
    PageableResponse<WorkerResponse> getAllActiveUserWithSortingSearching(int pageNumber, int pageSize, String sortBy, String sortDir);


    //  PageableResponse<WorkerResponse> searchActiveUserWithDynamicFields(String query, Pageable pageable);

}
