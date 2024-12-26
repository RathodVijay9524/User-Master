package com.vijay.User_Master.service.generics;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdvancedCrudService<Res, ID> {
    List<Res> findAll();
    Res findById(ID id) throws Exception;
    void softDelete(ID id) throws Exception;// findById(id); setIsDeleted(true);  setDeletedOn(LocalDateTime.now());
    void restore(ID id) throws Exception;//findById(id) setIsDeleted(false);setDeletedOn(null);
    void hardDelete(ID id) throws Exception;
    List<Res> getRecycleBin();  //CommonUtil.getLoggedInUser().getId();findByCreatedByAndIsDeletedTrue(userId);
    Res copy(ID id) throws Exception;
    Page<Res> findAll(Pageable pageable);
    Page<Res> search(String query, Pageable pageable);
}
