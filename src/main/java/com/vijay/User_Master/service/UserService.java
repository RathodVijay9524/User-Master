package com.vijay.User_Master.service;



import com.vijay.User_Master.dto.UserRequest;
import com.vijay.User_Master.dto.UserResponse;
import com.vijay.User_Master.service.generics.iCrudService;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService extends iCrudService<UserRequest, UserResponse,Long> {

    UserResponse getCurrentUser();
   



}
