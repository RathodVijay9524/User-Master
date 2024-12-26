package com.vijay.User_Master.service;



import com.vijay.User_Master.dto.UserRequest;
import com.vijay.User_Master.dto.UserResponse;
import com.vijay.User_Master.service.generics.iCrudService;

public interface UserService extends iCrudService<UserRequest, UserResponse,Long> {

    UserResponse getCurrentUser();



}
