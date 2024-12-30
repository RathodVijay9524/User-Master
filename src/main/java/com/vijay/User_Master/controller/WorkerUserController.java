package com.vijay.User_Master.controller;

import com.vijay.User_Master.Helper.ExceptionUtil;
import com.vijay.User_Master.dto.FavouriteEntryResponse;
import com.vijay.User_Master.dto.PageableResponse;
import com.vijay.User_Master.dto.WorkerResponse;
import com.vijay.User_Master.service.WorkerUserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveWorkerUser() {
        return ExceptionUtil.createBuildResponse(workerUserService.findAllActiveUsers(), HttpStatus.OK);
    }

    @GetMapping("/recycle")
    public ResponseEntity<?> getSoftDeletedUser(@PageableDefault(size = 10, sort = "name") Pageable pageable) {
        PageableResponse<WorkerResponse> pages = workerUserService.getRecycleBin(pageable);
        return ExceptionUtil.createBuildResponse(pages, HttpStatus.OK);
    }

    @DeleteMapping("/recycle/delete-all")
    public ResponseEntity<?> deleteAllUserSuperUserId(@PageableDefault(size = 10, sort = "name") Pageable pageable) {
        workerUserService.emptyRecycleBin(pageable);
        return ExceptionUtil.createBuildResponse("Deleted All item form Recycle Bin..! now Empty Recycle Bin. !", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault(size = 10, sort = "name") Pageable pageable) {
        PageableResponse<WorkerResponse> pages = workerUserService.findAll(pageable);
        return ExceptionUtil.createBuildResponse(pages, HttpStatus.OK);
    }

    @GetMapping("/pageable")
    public ResponseEntity<?> getActiveUserPageableWithSortAndSearch(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "2", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<WorkerResponse> listUsers = workerUserService.getAllActiveUserWithSortingSearching(pageNumber, pageSize, sortBy, sortDir);
        return ExceptionUtil.createBuildResponse(listUsers, HttpStatus.OK);
    }

    // http://localhost:9091/api/v1/workers/search?query=ajay&page=0&size=10&sort=name,asc
    @GetMapping("/search")
    public ResponseEntity<?> searchWorkers(@RequestParam("query") String query, @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        try {
            PageableResponse<WorkerResponse> PageableResponse = workerUserService.searchItemsWithDynamicFields(query, pageable);
            return ExceptionUtil.createBuildResponse(PageableResponse, HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/favorite-list")
    public ResponseEntity<?> getAllFavoriteWorkerUser() throws Exception {
        List<FavouriteEntryResponse> userFavoriteWorkerUsers = workerUserService.getUserFavoriteWorkerUsers();
        return ExceptionUtil.createBuildResponse(userFavoriteWorkerUsers, HttpStatus.OK);
    }

    @PostMapping("/favorite/{workerId}")
    public ResponseEntity<?> favoriteWorkerUser(@PathVariable Long workerId) throws Exception {
        workerUserService.favoriteWorkerUser(workerId);
        return ExceptionUtil.createBuildResponseMessage("Worker added to favorites successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/favorite/{id}")
    public ResponseEntity<?> unFavoriteWorkerUser(@PathVariable Long id) throws Exception {
        workerUserService.unFavoriteWorkerUser(id);
        return ExceptionUtil.createBuildResponseMessage("Worker removed from favorites successfully!", HttpStatus.OK);

    }

}
