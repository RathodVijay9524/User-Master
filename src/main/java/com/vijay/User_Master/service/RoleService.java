package com.vijay.User_Master.service;

import com.vijay.User_Master.dto.RoleRequest;
import com.vijay.User_Master.dto.RoleResponse;
import com.vijay.User_Master.service.generics.iCrudService;

public interface RoleService extends iCrudService<RoleRequest, RoleResponse,Long> {
}
