package com.vijay.User_Master.service.impl;

import com.vijay.User_Master.dto.form.EmailForm;
import com.vijay.User_Master.entity.AccountStatus;
import com.vijay.User_Master.entity.User;
import com.vijay.User_Master.exceptions.BadApiRequestException;
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
    private EmailService emailService;

    @Override
    public Boolean verifyAccount(Long uid, String verificationCode) throws Exception {
        log.info("Verifying account for user ID: {}", uid);
        User user = userRepo.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("USER", "ID", uid));

        if (user.getAccountStatus().getVerificationCode() == null) {
            throw new BadApiRequestException("Account already verified");
        }
        if (user.getAccountStatus().getVerificationCode().equals(verificationCode)) {
            AccountStatus status = user.getAccountStatus();
            status.setIsActive(true);
            status.setVerificationCode(null);
            User verrifiedUser = userRepo.save(user);
            sendMailSuccessMessageToUser(verrifiedUser);
            log.info("Account verification successful for user ID: {}", uid);
            return true;
        } else {
            log.warn("Invalid verification code provided for user ID: {}", uid);
            return false;
        }
    }

    private void sendMailSuccessMessageToUser(User verifiedUser) throws Exception {
        String emailBody = buildVerificationSuccessEmail(verifiedUser);
        EmailForm emailRequest = EmailForm.builder()
                .to(verifiedUser.getEmail())
                .subject("Account Verification Successful")
                .title("Congratulations " + verifiedUser.getName() + "!")
                .message(emailBody).build();
        emailService.sendEmail(emailRequest);
    }

    private String buildVerificationSuccessEmail(User verifiedUser) {
        return "<html>"
                + "<body>"
                + "<h3>Congratulations "
                + verifiedUser.getName()
                + "!</h3>" + "<p>Your account has been successfully verified.</p>"
                + "<p>You can now enjoy all the features of our service.</p>"
                + "<p>Best regards,<br/>Vijay Rathod</p>"
                + "</body>"
                + "</html>";
    }
}



