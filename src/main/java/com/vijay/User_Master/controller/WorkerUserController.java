package com.vijay.User_Master.controller;

import com.vijay.User_Master.Helper.ExceptionUtil;
import com.vijay.User_Master.service.WorkerUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workers")
@AllArgsConstructor
public class WorkerUserController {

    private final WorkerUserService workerUserService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getWorkerUserById(@PathVariable Long id) throws Exception {
        return ExceptionUtil.createBuildResponse(workerUserService.findById(id), HttpStatus.OK);
    }
    @GetMapping("/soft/{id}")
    public ResponseEntity<?> softDeleteUserById(@PathVariable Long id) throws Exception {
        workerUserService.softDelete(id);
        return ExceptionUtil.createBuildResponseMessage("User Deleted, User Available in RecycleBin bean", HttpStatus.OK);
    }
    @GetMapping("/restore/{id}")
    public ResponseEntity<?> restoreDeleteUserById(@PathVariable Long id) throws Exception {
        workerUserService.restore(id);
        return ExceptionUtil.createBuildResponseMessage("User Restored Successfully,", HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletingUserByIdFromRecycleBin(@PathVariable Long id) throws Exception {
        workerUserService.hardDelete(id);
        return ExceptionUtil.createBuildResponseMessage("User Deleted Permanently,", HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<?> getAll(){
        return ExceptionUtil.createBuildResponse(workerUserService.findAll(),HttpStatus.OK);
    }
    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveWorkerUser(){
        return ExceptionUtil.createBuildResponse(workerUserService.findAllActiveUsers(),HttpStatus.OK);
    }
    @GetMapping("/recycle")
    public ResponseEntity<?> getSoftDeletedUser(){
        return ExceptionUtil.createBuildResponse(workerUserService.getRecycleBin(),HttpStatus.OK);
    }


}