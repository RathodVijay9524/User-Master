package com.vijay.User_Master.service.impl;

import com.vijay.User_Master.entity.AccountStatus;
import com.vijay.User_Master.entity.User;
import com.vijay.User_Master.exceptions.ResourceNotFoundException;
import com.vijay.User_Master.repository.UserRepository;
import com.vijay.User_Master.service.HomeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
@Log4j2
public class HomeServiceImpl implements HomeService {

    private UserRepository userRepo;
    @Override
    public Boolean verifyAccount(Long uid, String verificationCode) {
        log.info("Verifying account for user ID: {}", uid);
        User user = userRepo.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("USER", "ID", uid));

        if(user.getAccountStatus().getVerificationCode()==null)
        {
            throw new RuntimeException("Account already verified");
        }
        if (user.getAccountStatus().getVerificationCode().equals(verificationCode)) {
            AccountStatus status = user.getAccountStatus();
            status.setIsActive(true);
            status.setVerificationCode(null);
            userRepo.save(user);

            log.info("Account verification successful for user ID: {}", uid);
            return true;
        } else {
            log.warn("Invalid verification code provided for user ID: {}", uid);
            return false;
        }
    }
}



