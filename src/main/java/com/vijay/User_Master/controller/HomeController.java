package com.vijay.User_Master.controller;

import com.vijay.User_Master.Helper.ExceptionUtil;
import com.vijay.User_Master.dto.form.ForgotPasswordForm;
import com.vijay.User_Master.exceptions.ResourceNotFoundException;
import com.vijay.User_Master.service.AuthService;
import com.vijay.User_Master.service.HomeService;
import com.vijay.User_Master.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/home")
@AllArgsConstructor
public class HomeController {


    private HomeService homeService;

    private AuthService authService;

    //http://localhost:9091/api/v1/home/verify?uid=10&&code=39796311-9f1d-4fa7-8773-cbc3a03aacc2
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserAccount(@RequestParam Long uid, @RequestParam String code) throws Exception {
        Boolean verifyAccount = homeService.verifyAccount(uid, code);
        if (verifyAccount)
            return ExceptionUtil.createBuildResponseMessage("Account verification success", HttpStatus.OK);
        return ExceptionUtil.createErrorResponseMessage("Invalid Verification link", HttpStatus.BAD_REQUEST);
    }
    // you can reset password using email or username
    //http://localhost:9091/api/v1/home/forgot-password?usernameOrEmail=vijayrathod9524@gmail.com
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordForm form, @RequestParam String usernameOrEmail) {
        try {
            authService.forgotPassword(form, usernameOrEmail);
            return ExceptionUtil.createBuildResponseMessage("Password reset successfully", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return ExceptionUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return ExceptionUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ExceptionUtil.createErrorResponseMessage("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/welcome")
    public String hi() {
        return "Welcome to java Programing";
    }

    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        /*
         * This endpoint retrieves the CSRF token from the request attributes.
         *
         * CSRF tokens are used to prevent Cross-Site Request Forgery attacks by ensuring
         * that state-changing requests (POST, PUT, DELETE, etc.) originate from the
         * authenticated client.
         *
         * The token is fetched from the "_csrf" attribute, set by Spring Security, and
         * returned to the client so it can include the token in headers or request bodies
         * when making state-changing API calls.
         */
        return (CsrfToken) request.getAttribute("_csrf");
    }
}
