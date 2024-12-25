package com.vijay.User_Master.service.impl;

import com.vijay.User_Master.Helper.CommonUtils;
import com.vijay.User_Master.Helper.EmailUtils;
import com.vijay.User_Master.config.security.CustomUserDetails;
import com.vijay.User_Master.config.security.JwtTokenProvider;
import com.vijay.User_Master.config.security.model.LoginJWTResponse;
import com.vijay.User_Master.config.security.model.LoginRequest;
import com.vijay.User_Master.dto.UserRequest;
import com.vijay.User_Master.dto.UserResponse;
import com.vijay.User_Master.dto.form.ChangePasswordForm;
import com.vijay.User_Master.dto.form.EmailForm;
import com.vijay.User_Master.dto.form.ForgotPasswordForm;
import com.vijay.User_Master.dto.form.UnlockForm;
import com.vijay.User_Master.entity.AccountStatus;
import com.vijay.User_Master.entity.Role;
import com.vijay.User_Master.entity.User;
import com.vijay.User_Master.entity.Worker;
import com.vijay.User_Master.exceptions.BadApiRequestException;
import com.vijay.User_Master.exceptions.ResourceNotFoundException;
import com.vijay.User_Master.exceptions.UserAlreadyExistsException;
import com.vijay.User_Master.repository.RoleRepository;
import com.vijay.User_Master.repository.UserRepository;
import com.vijay.User_Master.repository.WorkerRepository;
import com.vijay.User_Master.service.AuthService;
import com.vijay.User_Master.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
@Log4j2
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ModelMapper mapper;
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;
    private EmailUtils emailUtils;
    private EmailService emailService;

/*
*     **************  when user register that time need to send temp password
*                 to unlock account in email then user can create new password and he or see can log in. *****
*
*        but in this application we are verifier user direct when he clicks on link... inside mail
*        so this method implemented for knowledge
*
* */
    @Override
    public boolean unlockAccount(UnlockForm form, String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> {
                    log.error("User with ID '{}' not found", usernameOrEmail);
                    return new ResourceNotFoundException("USER", "ID", usernameOrEmail);
                });
        // Check if the temporary password matches the user's current password
        if (!passwordEncoder.matches(form.getTempPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Temporary Password is incorrect");
        }
        // Ensure new password and confirm password match
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("New Password and Confirm Password do not match");
        }
        // Encode the new password and save it
        user.setPassword(passwordEncoder.encode(form.getNewPassword()));

        /*
         *   need to set status as unlock or Active, then user can log in
         * */
        AccountStatus accountStatus=AccountStatus.builder()
                .isActive(true)
                .build();
        user.setAccountStatus(accountStatus);

        userRepository.save(user);
        // Send a success email to the user
        String subject = "Congratulations! Your Account is Unlocked";
        String body = "Your account has been successfully unlocked. You can now log in with your new password.<br>Thank you.";
        emailUtils.sendEmail(user.getEmail(), subject, body);
        log.info("Account unlocked successfully for user ID: {}", user.getName());
        return true;
    }

    @Override
    public boolean forgotPassword(ForgotPasswordForm form, String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> {
                    log.error("User with ID '{}' not found", usernameOrEmail);
                    return new ResourceNotFoundException("USER", "ID", usernameOrEmail);
                });

        // Check if new password and confirm password match
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Encode the new password and save it
        String encodedNewPassword = passwordEncoder.encode(form.getNewPassword());
        user.setPassword(encodedNewPassword);
        userRepository.save(user);

        log.info("Password reset successfully for user ID: {}", user.getId());
        return true;
    }


    @Override
    public boolean changePassword(ChangePasswordForm form) {
        CustomUserDetails userDetails = CommonUtils.getLoggedInUser();
        User user = userRepository.findByEmail(userDetails.getEmail());
        // Log the incoming reset password request
        System.out.println("Resetting password for email: " + user.getEmail());
        // Check if the user exists
        if (user == null) {
            System.out.println("User not found with email: " + user.getEmail());
            return false;
        }
        if (!passwordEncoder.matches(form.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old Password is incorrect ");
        }
        String encodePasswordNewPassword = passwordEncoder.encode(form.getNewPassword());
        // Check if the old password matches the user's current password
        user.setPassword(encodePasswordNewPassword);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean existsByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.existsByUsername(usernameOrEmail)
                || userRepository.existsByEmail(usernameOrEmail)
                || workerRepository.existsByUsername(usernameOrEmail)
                || workerRepository.existsByEmail(usernameOrEmail);
    }

    @Override
    public LoginJWTResponse login(LoginRequest req) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(req.getUsernameOrEmail(), req.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(req.getUsernameOrEmail());

        User user = userRepository.findByEmail(req.getUsernameOrEmail());
        if (!user.getAccountStatus().getIsActive()) {
            log.warn("Account not active for user ID: {}", user.getId());
            throw new BadApiRequestException("Account is not active. Please activate your account.");
        }

        String token = jwtTokenProvider.generateToken(authentication);

        UserResponse response = mapper.map(userDetails, UserResponse.class);

        LoginJWTResponse jwtResponse = LoginJWTResponse.builder()
                .jwtToken(token)
                .user(response).build();

        return jwtResponse;
    }


    @Override
    public CompletableFuture<Object> registerForAdminUser(UserRequest request, String url) {
        log.info("Attempting to create a new admin user with username: {}", request.getUsername());
        return CompletableFuture.supplyAsync(() -> {
            if (existsByUsernameOrEmail(request.getUsername()) || existsByUsernameOrEmail(request.getEmail())) {
                log.error("Username '{}' or email '{}' already exists", request.getUsername(), request.getEmail());
                throw new UserAlreadyExistsException("Username or email is already taken");
            }
            User user = mapper.map(request, User.class);
            Role role = roleRepository.findByName("ROLE_NORMAL").orElseThrow(() -> {
                log.error("Role 'User' not found");
                return new BadApiRequestException("Role not found with name 'ROLE_ADMIN'");
            });
            user.setRoles(Set.of(role));
            //String tempPwd= PwdUtils.generateRandomPwd();
            //user.setPassword(tempPwd);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            AccountStatus accountStatus = AccountStatus.builder()
                    .isActive(false)
                    .passwordResetToken(null)
                    .verificationCode(UUID.randomUUID().toString())
                    .build();
            user.setAccountStatus(accountStatus);

            User savedUser = userRepository.save(user);

            // Send confirmation email to the new admin user
            if (!ObjectUtils.isEmpty(savedUser)) {
                // send email
                try {
                    emailSendForRegisterUser(savedUser, url);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
            log.info("Admin user with username '{}' created successfully", user.getUsername());
            return mapper.map(user, UserResponse.class);
        });
    }

    private void emailSendForRegisterUser(User savedUser, String url) throws Exception {

        String message = "Hi,<b>[[username]]</b> " + "<br> Your account register sucessfully.<br>"
                + "<br> Click the below link verify & Active your account <br>"
                + "<a href='[[url]]'>Click Here</a> <br><br>" + "Thanks,<br>Enotes.com";

        message = message.replace("[[username]]", savedUser.getName());
        message = message.replace("[[url]]", url + "/api/v1/home/verify?uid=" + savedUser.getId() + "&&code="
                + savedUser.getAccountStatus().getVerificationCode());

        EmailForm emailRequest = EmailForm.builder()
                .to(savedUser.getEmail())
                .title("Account Creating Confirmation")
                .subject("Account Created Success")
                .message(message)
                .build();
        emailService.sendEmail(emailRequest);
    }

    @Override
    public UserResponse registerForNormalUser(UserRequest request) {
        log.info("Attempting to create a new normal user with username: {}", request.getUsername());
        if (existsByUsernameOrEmail(request.getUsername()) || existsByUsernameOrEmail(request.getEmail())) {
            log.error("Username '{}' or email '{}' already exists", request.getUsername(), request.getEmail());
            throw new UserAlreadyExistsException("Username or email is already taken");
        }
        UserResponse currentUser = userService.getCurrentUser();
        User user = mapper.map(currentUser, User.class);

        Worker worker = mapper.map(request, Worker.class);
        worker.setPassword(passwordEncoder.encode(request.getPassword()));

        // Fetch the worker role from the repository
        Role workerRole = roleRepository.findByName("ROLE_WORKER")
                .orElseThrow(() -> new BadApiRequestException("Worker role not found."));

        // Initialize the roles field if it's null
        if (worker.getRoles() == null) {
            worker.setRoles(new HashSet<>());
        }
        // Add the worker role to the roles set
        worker.getRoles().add(workerRole);
        worker.setUser(user);
        workerRepository.save(worker);
        return mapper.map(worker, UserResponse.class);
    }

}










